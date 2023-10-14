from fastapi import FastAPI
import json
from src.models import queeReturn, queeRequest, quee

from datetime import datetime

queue_data: dict
with(open("src/loads_entry.json", "r")) as file:
    queue_data = json.loads("".join(file.readlines()))

app = FastAPI()

@app.post("/getload", response_model=queeReturn)
async def getLoad(req: queeRequest):
    data = queue_data.get("days")[datetime.now().weekday()]
    return queeReturn(**quee().init(data).__dict__)