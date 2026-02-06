# Projeto Fullstack Edital

Aplicação fullstack para gestão de artistas e álbuns, construída conforme o edital.

## Stack
- Frontend: React + TypeScript + Vite + Tailwind + RxJS
- Backend: Spring Boot 3 + JPA + PostgreSQL + Flyway + JWT + WebSocket (STOMP)
- Infra local: Docker Compose (Postgres + MinIO)

## Pré-requisitos
- Docker + Docker Compose
- Node.js (para rodar o frontend localmente)

## Como subir o backend (Docker)
Na raiz do projeto:
```cmd
docker compose up --build backend
```

Isso sobe:
- `fullstack-backend` (API em `http://localhost:8080`)
- `fullstack-postgres`
- `fullstack-minio`

## Como subir o frontend (local)
```cmd
cd frontend
npm install
npm run dev
```
Front em `http://localhost:5173`.

Obs.: se o PowerShell bloquear `npm`, rode no `cmd` ou ajuste a Execution Policy.

## Login
Credenciais padrão:
- Email: `admin@local`
- Senha: `admin`

Endpoint de login:
`POST /api/v1/auth/login`

## Funcionalidades principais
- CRUD de artistas e álbuns
- Busca, ordenação e paginação de artistas
- WebSocket: notificação de novo álbum no front
- Rate limit: 10 req/min por usuário/IP
- Health checks: liveness/readiness
- Sincronização de regionais (integração externa)

## WebSocket (notificação de novo álbum)
Backend:
- Endpoint SockJS: `http://localhost:8080/ws`
- Tópico: `/topic/albums`

Frontend já conecta automaticamente quando autenticado.

## Health checks
- Liveness: `GET /api/v1/health/liveness`
- Readiness: `GET /api/v1/health/readiness`

Ambos estão liberados sem autenticação.

## Sincronização de regionais
Endpoint:
`POST /api/v1/regionais/sync` (requer JWT)

Exemplo no `cmd`:
```cmd
set TOKEN=COLE_AQUI_O_ACCESS_TOKEN
curl.exe -i -X POST http://localhost:8080/api/v1/regionais/sync -H "Authorization: Bearer %TOKEN%"
```

Regra de sincronização:
- Se não existir no endpoint remoto: inativa.
- Se for novo: insere.
- Se mudou o nome: inativa o antigo e cria novo.

URL externa configurável em `backend/src/main/resources/application.yml`:
```
regional:
  sync-url: ${REGIONAL_SYNC_URL:https://servicodados.ibge.gov.br/api/v1/localidades/mesorregioes}
```

## Rate limit
Limite: **10 req/min** por usuário/IP.  
Retorna `429` com JSON:
```json
{"message":"Rate limit excedido (10 req/min)."}
```

## Testes unitários (backend)
```cmd
cd backend
mvn -q test
```

Cobertura básica:
- `JwtService`
- `AuthService`
- `RateLimitFilter`

## Variáveis de ambiente úteis
Backend:
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_ACCESS_TTL_MINUTES`, `JWT_REFRESH_TTL_DAYS`
- `CORS_ALLOWED_ORIGINS`
- `MINIO_ENDPOINT`, `MINIO_PUBLIC_ENDPOINT`, `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY`, `MINIO_BUCKET`
- `REGIONAL_SYNC_URL`

Frontend:
- `VITE_API_URL` (default `http://localhost:8080`)
- `VITE_WS_URL` (default `http://localhost:8080/ws`)

## Status do item Upload de capas (MinIO)
O fluxo de upload foi iniciado, mas **não está concluído** no momento.  
Este item foi explicitamente colocado em pausa.

