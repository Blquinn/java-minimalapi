.PHONY : bootstrap run-db-migrations shutdown

bootstrap:
	docker compose up -d
	$(MAKE) run-db-migrations

run-db-migrations:
	goose -dir db/migrations postgres 'postgres://postgres:password@localhost:5432/postgres' up

shutdown:
	docker compose down

