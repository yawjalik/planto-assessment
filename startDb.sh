#!/usr/bin/env bash
set -e

docker run --name csv-editor-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16.1-alpine3.19