# 📒 Agenda de Contactos

Aplicación web full-stack desarrollada como **Actividad 1 de Pasantía**, que permite a los usuarios gestionar su propia agenda de contactos. Cada usuario puede registrarse, iniciar sesión y administrar sus contactos de forma independiente.

---

## 🗂️ Estructura del repositorio

```
pasantia-act1-agenda/
├── agenda-contactos-backend/   # API REST con Spring Boot
├── agenda-contactos-frontend/  # SPA con React + Vite
└── agenda-contactos.sql        # Dump de la base de datos PostgreSQL
```

---

## ✨ Funcionalidades

- **Registro e inicio de sesión** de usuarios
- **Modificación de perfil** (nombre, email, contraseña)
- **Gestión de contactos**: crear, listar, editar y eliminar
- **Búsqueda de contactos** por nombre
- Cada usuario visualiza únicamente sus propios contactos

---

## 🛠️ Tecnologías utilizadas

### Backend
| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| Spring Data JPA | — |
| PostgreSQL | 18 |
| Lombok | 1.18.38 |
| Maven | — |

### Frontend
| Tecnología | Versión |
|---|---|
| React | 19 |
| Vite | 8 |
| React Router DOM | 7 |
| Axios | 1.x |

---

## 🏗️ Arquitectura

### Backend — Arquitectura en capas

```
src/main/java/com/pasantia/
├── config/         # Configuración CORS
├── controller/     # Endpoints REST (ContactoController, UsuarioController)
├── service/        # Interfaces de servicio
│   └── impl/       # Implementaciones de lógica de negocio
├── repository/     # Repositorios JPA
├── entity/         # Entidades JPA (Usuario, Contacto)
└── dto/            # Objetos de transferencia de datos
```

**Endpoints principales:**

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/usuario` | Registrar usuario |
| `POST` | `/api/usuario/login` | Iniciar sesión |
| `GET` | `/api/usuario/{id}` | Obtener usuario por ID |
| `PUT` | `/api/usuario/{id}` | Modificar perfil |
| `POST` | `/api/contacto?usuarioId=` | Crear contacto |
| `GET` | `/api/contacto/usuario/{usuarioId}` | Listar contactos de un usuario |

### Frontend — Estructura de carpetas

```
src/
├── components/     # Componentes reutilizables (ContactCard, ContactGrid, Navbar)
│   └── atomic/     # Componentes base (Button)
├── pages/          # Vistas (Login, Register, Dashboard, Perfil)
├── services/       # Llamadas a la API (axios)
├── hooks/          # Custom hooks (useAuth, useContacto)
└── assets/         # Imágenes y recursos estáticos
```

### Base de datos

```sql
-- Tabla de usuarios
Usuario(id, nombre, email, password, fechaCreacion)

-- Tabla de contactos (relacionada con usuario)
Contacto(id, nombre, apellido, telefono, email, direccion, observaciones, usuario_id)
```

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos previos

- Java 17+
- Maven
- Node.js + npm (o bun)
- PostgreSQL

---

### 1. Base de datos

Crear la base de datos e importar el dump incluido:

```bash
psql -U postgres -c "CREATE DATABASE agenda;"
psql -U postgres -d agenda -f agenda-contactos.sql
```

> La configuración por defecto espera: host `localhost:5432`, usuario `postgres`, contraseña `123`.  
> Podés cambiar estos valores en `agenda-contactos-backend/src/main/resources/application.properties`.

---

### 2. Backend

```bash
cd agenda-contactos-backend
./mvnw spring-boot:run
```

La API quedará disponible en `http://localhost:8080`.

---

### 3. Frontend

```bash
cd agenda-contactos-frontend
npm install
npm run dev
```

La aplicación quedará disponible en `http://localhost:5173`.

> El frontend apunta por defecto a `http://localhost:8080/api`. Podés modificarlo en `src/services/api.js`.

---

## ⚙️ Configuración de CORS

El backend tiene configurado CORS para aceptar solicitudes desde `http://localhost:5173`. Si cambiás el puerto del frontend, actualizá el valor en:

```
agenda-contactos-backend/src/main/java/com/pasantia/config/CorsConfig.java
```

---

## 📋 Criterios de evaluación

Este proyecto fue desarrollado cumpliendo los siguientes criterios:

- ✅ Funcionamiento completo de las funcionalidades requeridas
- ✅ Arquitectura en capas (controller → service → repository)
- ✅ Calidad del código y uso de DTOs
- ✅ Repositorio público en GitHub
- ✅ README con instrucciones de instalación y ejecución
