import random

from pydantic import BaseModel, model_validator
from datetime import datetime

class quee():
    # bankId: int
    individualManagers: int #Активных менеджеров физ. лиц / 1
    legalManagers: int  #Активных менеджеров юр. лиц / 1
    individualQueue: int #Кол-во человек в физ. очереди / 1
    legalQueue: int #Кол-во человек в юр. очереди / 1
    indivWaitingTime: int #Примерное время ожидания физ. очереди / 1
    legalWaitingTime: int  # Примерное время ожидания физ. очереди / 1

    def init(self, kwargs: dict):
        # super().__init__()
        hour = str(datetime.now().hour)


        self.individualQueue = random.randint(kwargs.get("queueIndividual").get(hour).get("min"),
                                              kwargs.get("queueIndividual").get(hour).get("max"))
        self.individualManagers =  self.individualQueue / 4 if (self.individualQueue / 4) < 3 else 3

        self.legalQueue = random.randint(kwargs.get("queueLegal").get(hour).get("min"),
                                         kwargs.get("queueLegal").get(hour).get("max"))
        self.legalManagers = self.legalQueue / 3 if (self.legalQueue / 3) < 2 else 3
        try:
            self.indivWaitingTime = int(self.individualQueue / (self.individualManagers * kwargs.get("speedIndQueue")))
        except ZeroDivisionError:
            self.indivWaitingTime = 0
        try:
            self.legalWaitingTime = int(self.legalQueue / (self.legalManagers * kwargs.get("speedLegalQueue")))
        except ZeroDivisionError:
            self.legalWaitingTime = 0

        return self


class queeReturn(BaseModel):
    individualManagers: int #Активных менеджеров физ. лиц / 1
    legalManagers: int  #Активных менеджеров юр. лиц / 1
    individualQueue: int #Кол-во человек в физ. очереди / 1
    legalQueue: int #Кол-во человек в юр. очереди / 1
    indivWaitingTime: int #Примерное время ожидания физ. очереди / 1
    legalWaitingTime: int  # Примерное время ожидания физ. очереди / 1

class queeRequest(BaseModel):
    bankId: int