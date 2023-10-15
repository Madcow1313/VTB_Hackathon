all: run

build:
	docker-compose build

run:
	docker-compose up

down:
	docker-compose down

uvicorn:
	uvicorn --port 8001 main:app --reload
