import logging
from contextlib import asynccontextmanager
from fastapi import FastAPI
import httpx
from DataIngest.Endpoints.FinancialData import financial_router

# Configuración básica de logs para monitorear el inicio y apagado
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


# ==========================================
# GESTIÓN DEL CICLO DE VIDA (LIFESPAN)
# ==========================================
@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Maneja los eventos de inicio y apagado de la aplicación.
    Ideal para inicializar clientes de bases de datos, colas o HTTP.
    """
    logger.info("Iniciando la aplicación y configurando recursos...")

    # Inicializamos el cliente HTTP asíncrono global
    # Configuramos un timeout prudente (ej. 10 segundos) para no bloquear peticiones
    app.state.http_client = httpx.AsyncClient(
        timeout=httpx.Timeout(10.0), headers={"User-Agent": "MiAppFastAPI/1.0.0"}
    )

    yield  # Aquí es donde la aplicación "corre" y atiende peticiones

    # Al apagar la aplicación, cerramos el cliente limpiamente
    logger.info("Cerrando recursos y apagando la aplicación...")
    await app.state.http_client.aclose()


# ==========================================
# INICIALIZACIÓN DE FASTAPI
# ==========================================
app = FastAPI(
    title="Cliente de Consumo de APIs",
    description="Microservicio base para consumir datos de servicios externos de forma asíncrona.",
    version="1.0.0",
    lifespan=lifespan,
)


# ==========================================
# ENDPOINTS DE CONTROL (HEALTH CHECK)
# ==========================================
@app.get("/health", tags=["Mantenimiento"])
async def health_check():
    """
    Endpoint simple para verificar que el servicio está vivo.
    """
    return {
        "status": "healthy",
        "http_client_initialized": hasattr(app.state, "http_client"),
    }


# Aquí es donde más adelante registraremos los routers de nuestros módulos
app.include_router(financial_router)
# app.include_router(usuarios.router)
# app.include_router(productos.router)
