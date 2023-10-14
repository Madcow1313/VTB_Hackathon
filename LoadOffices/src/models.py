import random

from pydantic import BaseModel
from typing import Literal
from datetime import datetime
from typing import Any

class queeReturn(BaseModel):
    bankId: int
    individualManagers: int #Активных менеджеров физ. лиц
    legalManagers: int  #Активных менеджеров юр. лиц
    individualQueue: int #Кол-во человек в физ. очереди
    legalQueue: int #Кол-во человек в юр. очереди
    waitingTime

    def init(self, **kwargs):
        self.bankId = kwargs.get(id)
        self.individualQueue =


class queeRequest(BaseModel):
    bankId:int