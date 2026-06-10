from fastapi import Security
from fastapi import APIRouter, HTTPException, status, Depends, Request
from fastapi.security import APIKeyHeader
import logging
import os
import asyncio
import secrets
import httpx
import yfinance as yf

# Logging configuration for request auditing
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

financial_router = APIRouter()

# =====================================================================
# 1. SECURITY CONFIGURATION AND ENVIRONMENT VARIABLES
# =====================================================================

FINNHUB_API_KEY = os.getenv("FinHUBApiKEY")
ALPHA_VANTAGE_KEY = os.getenv("AlphaVantageApiKEY")


class APIKeyValidator:
    """
    API Key validator to secure microservice endpoints.
    Uses constant-time comparison to mitigate timing attacks.
    """

    def __init__(self, header_name: str = "X-FinHub-Token"):
        self.api_key_header = APIKeyHeader(name=header_name, auto_error=True)
        # Prioritize dedicated API key if configured, otherwise use FinHUBApiKEY for backwards compatibility
        self.expected_key = os.getenv("MPS_API_KEY") or FINNHUB_API_KEY
        if not self.expected_key:
            logger.warning(
                "WARNING: Neither 'MPS_API_KEY' nor 'FinHUBApiKEY' is configured in the environment. "
                "API requests will fail with a 500 error."
            )

    async def __call__(
        self,
        api_key: str = Security(APIKeyHeader(name="X-FinHub-Token", auto_error=True)),
    ):
        if not self.expected_key:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail="Authentication service is not properly configured on the server.",
            )

        # Constant-time secure comparison
        if not secrets.compare_digest(api_key, self.expected_key):
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid or unauthorized API Key for this microservice.",
            )
        return api_key


# Validator instance for use as FastAPI dependency
validate_api_key = APIKeyValidator()

# =====================================================================
# 2. DATA CONSUMPTION ENDPOINTS (ALL PROTECTED)
# =====================================================================


@financial_router.get("/provider/finnhub/{symbol}", tags=["Stock Information"])
async def test_finnhub(
    symbol: str, request: Request, api_key: str = Depends(validate_api_key)
):
    """
    [Stock] Validates Finnhub connection by fetching current quote.
    """
    client = request.app.state.http_client
    url = "https://finnhub.io/api/v1/quote"
    params = {
        "symbol": symbol.upper(),
        "token": FINNHUB_API_KEY,
    }

    data = await __api_calls(
        client=client,
        url=url,
        params=params,
        error_prefix="Finnhub error",
    )
    return {"provider": "Finnhub", "status": "OK", "data": data}


@financial_router.get("/provider/alphavantage/{symbol}", tags=["Stock Information"])
async def test_alphavantage(
    symbol: str, request: Request, api_key: str = Depends(validate_api_key)
):
    """
    [Stock] Validates AlphaVantage connection by fetching global quote.
    Note: Free tier has a strict limit (5 requests per minute).
    """
    client = request.app.state.http_client
    url = "https://www.alphavantage.co/query"
    params = {
        "function": "GLOBAL_QUOTE",
        "symbol": symbol.upper(),
        "apikey": ALPHA_VANTAGE_KEY,
    }

    data = await __api_calls(
        client=client,
        url=url,
        params=params,
        error_prefix="AlphaVantage error",
    )

    # Alpha Vantage rate limiting detection (returns 200 with error in body)
    if "Note" in data:
        raise HTTPException(
            status_code=429,
            detail="Alpha Vantage rate limit reached. Please try again later.",
        )

    if "Error Message" in data:
        raise HTTPException(
            status_code=400,
            detail=f"Alpha Vantage error: {data['Error Message']}",
        )

    return {"provider": "AlphaVantage", "status": "OK", "data": data}


@financial_router.get("/provider/yfinance/{symbol}", tags=["Stock Information"])
async def test_yfinance(symbol: str):
    """
    [Stock] Validates data extraction using the yfinance library.
    Uses asyncio.to_thread to prevent blocking the event loop.
    """
    try:
        ticker = yf.Ticker(symbol.upper())
        # Execute synchronous yfinance call in a separate thread
        history = await asyncio.to_thread(ticker.history, period="1d")

        if history.empty:
            raise HTTPException(
                status_code=404,
                detail=f"No data found in Yahoo Finance for {symbol}",
            )

        # Extract basic data from the returned DataFrame
        last_close = history["Close"].iloc[-1]
        volume = int(history["Volume"].iloc[-1])

        return {
            "provider": "Yahoo Finance",
            "status": "OK",
            "data": {
                "symbol": symbol.upper(),
                "last_close": last_close,
                "volume": volume,
            },
        }
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=500, detail=f"Error processing yfinance data: {str(e)}"
        )


@financial_router.get("/provider/reddit/{subreddit}", tags=["Sentiment Information"])
async def test_reddit(
    subreddit: str, request: Request, api_key: str = Depends(validate_api_key)
):
    """
    [Sentiment] Validates public feed consumption from a subreddit (e.g., wallstreetbets).
    Note: Reddit requires a custom User-Agent or will return HTTP 429/403 error.
    """
    client = request.app.state.http_client
    url = f"https://www.reddit.com/r/{subreddit}/hot.json"
    headers = {"User-Agent": "FastAPI-Financial-Bot/1.0"}
    params = {"limit": 5}

    reddit_data = await __api_calls(
        client=client,
        url=url,
        params=params,
        headers=headers,
        error_prefix="Reddit error",
    )

    # Extract only post titles from hot threads
    posts = [
        {
            "title": post["data"]["title"],
            "score": post["data"]["score"],
        }
        for post in reddit_data["data"]["children"]
    ]

    return {
        "provider": "Reddit",
        "status": "OK",
        "subreddit": subreddit,
        "data": posts,
    }


# COMMON METHODS
async def __api_calls(
    client: httpx.AsyncClient,
    url: str,
    params: dict = None,
    headers: dict = None,
    method: str = "GET",
    error_prefix: str = "Provider error",
):
    """
    Generic function to make HTTP API calls with error handling.

    Args:
        client: HTTPX async client
        url: Endpoint URL
        params: Query parameters (optional)
        headers: Request headers (optional)
        method: HTTP method (GET, POST, etc.)
        error_prefix: Prefix for error messages

    Returns:
        JSON response data

    Raises:
        HTTPException: With appropriate status code and message
    """
    try:
        response = await client.request(
            method=method, url=url, params=params, headers=headers
        )
        response.raise_for_status()
        return response.json()

    except httpx.HTTPStatusError as e:
        raise HTTPException(
            status_code=e.response.status_code,
            detail=f"{error_prefix}: {e.response.text}",
        )
    except httpx.RequestError as e:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail=f"{error_prefix}: Connection error - {str(e)}",
        )
