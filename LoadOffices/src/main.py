from fastapi import FastAPI
import models
import json

queue_data: dict
with(open("loads_entry.json", "r") as file):
    queue_data = json.loads("".join(file.readlines()))

app = FastAPI()

@app.get("/")
def read_root():
    return {"1Hello": "World"}

@app.post("/getload", response_model=models.queeReturn)
async def getLoad(req: models.queeRequest):
    return 0