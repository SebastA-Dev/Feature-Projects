class FinancialOutput:
    def __init__(self, symbol: str, data: dict, provider: str):
        self.symbol = symbol
        self.data = data
        self.provider = provider
