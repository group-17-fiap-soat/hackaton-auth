# 🎥 Hackaton Auth - Sistema de Autenticação

Sistema de autenticação desenvolvido em Kotlin com Spring Boot para o processamento de vídeos, parte de uma arquitetura de microserviços.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Execução](#execução)
- [API Endpoints](#api-endpoints)
- [Deploy](#deploy)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Contribuição](#contribuição)

## 🚀 Sobre o Projeto

O **Hackaton Auth** é um microserviço de autenticação que faz parte de um sistema distribuído de processamento de vídeos. Este serviço é responsável por:

- 🔐 Autenticação e autorização de usuários
- 🎫 Geração e validação de tokens JWT
- 👤 Gerenciamento de usuários (registro, login)
- 🌐 Interface web para interação com o sistema
- 🔗 Integração com microserviço de processamento de vídeos

## 🏗️ Arquitetura

### Microserviços
- **🔐 Auth Service** (este projeto): `http://localhost:8082`
  - Autenticação, registro e tokens JWT
- **🎥 Video Service**: `http://localhost:8080`
  - Upload, processamento e download de vídeos

### Padrões Arquiteturais
- **Clean Architecture**: Separação clara entre camadas
- **Hexagonal Architecture**: Adapters, Use Cases e Entities
- **Microserviços**: Comunicação via REST APIs com JWT

### Estrutura de Camadas
```
src/main/kotlin/hackaton/fiapx/
├── adapters/           # Camada de adaptadores
│   ├── controllers/    # Controllers REST e Web
│   ├── gateways/      # Gateways de dados
│   └── presenters/    # Mapeadores de dados
├── commons/           # Configurações e utilitários
├── entities/          # Entidades de domínio
├── usecases/          # Casos de uso
└── FiapxApplication.kt # Classe principal
```

## 🛠️ Tecnologias Utilizadas

### Backend
- **Kotlin** 1.9.25
- **Spring Boot** 3.5.4
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT** (JSON Web Tokens) - Autenticação stateless

### Banco de Dados
- **PostgreSQL** 16 - Banco de dados principal

### Frontend
- **Thymeleaf** - Template engine
- **HTML5/CSS3/JavaScript** - Interface web
- **Bootstrap** - Framework CSS responsivo

### DevOps & Deploy
- **Docker** - Containerização
- **Docker Compose** - Orquestração local
- **Kubernetes** - Deploy em produção
- **Gradle** - Build e gerenciamento de dependências

### Observabilidade
- **OpenAPI/Swagger** - Documentação da API
- **Spring Boot Actuator** - Métricas e saúde

## 📋 Pré-requisitos

- **Java 21** ou superior
- **Docker** e **Docker Compose**
- **PostgreSQL** 16 (se executar localmente)
- **Gradle** (incluído via wrapper)

## 🔧 Instalação

### 1. Clone o repositório
```bash
git clone <repository-url>
cd hackaton-auth
```

### 2. Configure as variáveis de ambiente
Crie um arquivo `.env` na raiz do projeto:
```env
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE=hackaton
DATABASE_USER=hackaton
DATABASE_PASSWORD=hackaton
JWT_SECRET=sua-chave-secreta-jwt-aqui
```

### 3. Execute com Docker Compose
```bash
docker-compose up -d
```

Ou execute localmente:
```bash
./gradlew bootRun
```

## ⚙️ Configuração

### Perfis de Ambiente

#### Produção (`application.yml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DATABASE_HOST}:${DATABASE_PORT}/${DATABASE}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
```

#### Desenvolvimento (`application-local.yml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/hackaton
    username: hackaton
    password: hackaton
```

### Portas de Serviço
- **Auth Service**: 8082
- **Video Service**: 8080
- **PostgreSQL**: 5435 (Docker) / 5432 (Local)

## 🚀 Execução

### Docker (Recomendado)
```bash
# Iniciar todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Parar serviços
docker-compose down
```

### Local
```bash
# Iniciar PostgreSQL (se não usar Docker)
# Configurar banco conforme application-local.yml

# Executar aplicação
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Acesso
- **Interface Web**: http://localhost:8082
- **API**: http://localhost:8082/api
- **Swagger UI**: http://localhost:8082/swagger-ui.html

## 📡 API Endpoints

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Fazer login |

### Interface Web
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/` | Página inicial |
| GET | `/login` | Página de login |
| GET | `/register` | Página de registro |
| GET | `/home` | Dashboard (autenticado) |

### Exemplo de Requisições

#### Registro
```json
POST /api/auth/register
{
  "name": "João Silva",
  "email": "joao@email.com",
  "pass": "senha123"
}
```

#### Login
```json
POST /api/auth/login
{
  "email": "joao@email.com",
  "pass": "senha123"
}
```

#### Resposta do Login
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 🚢 Deploy

### Kubernetes
```bash
# Deploy no cluster
kubectl apply -f k8s/

# Verificar status
kubectl get pods
kubectl get services
```

### Docker Build Manual
```bash
# Build da imagem
docker build -t hackaton-auth:latest .

# Executar container
docker run -p 8082:8082 \
  -e DATABASE_HOST=postgres \
  -e DATABASE_USER=hackaton \
  -e DATABASE_PASSWORD=hackaton \
  hackaton-auth:latest
```

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
./gradlew test

# Testes específicos
./gradlew test --tests "AuthControllerTest"

# Com relatório
./gradlew test jacocoTestReport
```

### Cobertura de Testes
- Controllers: ✅ AuthController, AuthViewController
- Gateways: ✅ UserGateway
- Use Cases: ✅ Login, Register, GetUser
- Integração: ✅ FiapxApplication

## 📁 Estrutura do Projeto

```
hackaton-auth/
├── 📁 src/main/kotlin/hackaton/fiapx/
│   ├── 📁 adapters/
│   │   ├── 📁 controllers/          # REST e Web Controllers
│   │   │   ├── AuthController.kt    # API REST de autenticação
│   │   │   ├── AuthViewController.kt # Controllers web
│   │   │   └── operation/           # Interfaces de operações
│   │   ├── 📁 gateways/            # Acesso a dados
│   │   └── 📁 presenters/          # Mapeadores
│   ├── 📁 commons/                 # Configurações
│   │   ├── 📁 config/              # Configs Spring
│   │   ├── 📁 dto/                 # DTOs de request/response
│   │   └── 📁 exception/           # Exceções customizadas
│   ├── 📁 entities/                # Entidades JPA
│   ├── 📁 usecases/                # Lógica de negócio
│   │   ├── 📁 auth/                # Casos de uso de auth
│   │   └── 📁 user/                # Casos de uso de usuário
│   └── FiapxApplication.kt         # Classe principal
├── 📁 src/main/resources/
│   ├── 📁 static/                  # Assets web (CSS, JS)
│   ├── 📁 templates/               # Templates Thymeleaf
│   ├── application.yml             # Config produção
│   └── application-local.yml       # Config desenvolvimento
├── 📁 k8s/                         # Manifests Kubernetes
├── 📁 docker-compose.yaml          # Orquestração local
├── 📁 Dockerfile                   # Build da imagem
├── 📁 build.gradle.kts            # Build Gradle
└── 📁 README.md                   # Este arquivo
```

## 🔗 Integração entre Microserviços

### Fluxo de Autenticação
1. **Frontend** faz login via Auth Service (8082)
2. **Auth Service** valida credenciais e retorna JWT
3. **Frontend** usa JWT para acessar Video Service (8080)
4. **Video Service** valida JWT com Auth Service

### Headers de Autenticação
```javascript
const authHeaders = {
  'Authorization': `Bearer ${jwtToken}`
}
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Add: nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Notas de Desenvolvimento

### Comandos Úteis
```bash
# Build sem testes
./gradlew build -x test

# Limpar e buildar
./gradlew clean build

# Executar com profile específico
./gradlew bootRun --args='--spring.profiles.active=local'

# Verificar dependências
./gradlew dependencies
```

### Debugging
- Logs detalhados em `DEBUG` level
- Swagger UI disponível em desenvolvimento
- Health checks via Spring Actuator

---

**Desenvolvido para o Hackaton FIAP** 🚀

*Sistema de processamento de vídeos com arquitetura de microserviços*
