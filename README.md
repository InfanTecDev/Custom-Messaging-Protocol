# Custom Messaging Protocol

Un protocolo de comunicación personalizado implementado en Java para sistemas cliente-servidor utilizando sockets TCP.

## 📋 Descripción

Este proyecto implementa un sistema de mensajería en tiempo real que utiliza un protocolo de comunicación personalizado. El sistema permite a múltiples clientes conectarse a un servidor central para intercambiar mensajes privados, gestionar sesiones de usuario y mantener comunicación bidireccional eficiente.

## 🏗️ Arquitectura del Protocolo

### Estructura del Mensaje

El protocolo utiliza un formato de mensaje estructurado que consta de tres componentes principales:

```
Type:[TIPO]/Headers:Id-[ID]-Username-[USUARIO]-To-[DESTINATARIO]-From-[REMITENTE]/Body:[CUERPO_DEL_MENSAJE]
```

### Tipos de Mensaje

- **LOGIN**: Autenticación y conexión de usuarios al servidor
- **LOGOUT**: Desconexión ordenada de usuarios del servidor  
- **PRIVATE_MESSAGE**: Envío de mensajes privados entre usuarios

### Componentes del Protocolo

#### 1. MessageType (Enum)
Define los tipos de mensaje soportados por el protocolo:
```java
public enum MessageType {
    LOGIN, LOGOUT, PRIVATE_MESSAGE;
}
```

#### 2. ProtocolMessage (Record)
Estructura inmutable que encapsula un mensaje completo:
- **messageType**: Tipo de mensaje según MessageType
- **headers**: Mapa con metadatos (Id, Username, To, From)
- **body**: Contenido del mensaje

#### 3. ProtocolParser (Clase)
Utilidad para serialización y deserialización de mensajes:
- **format()**: Construye cadenas de mensaje formateadas
- **toProtocolMessage()**: Convierte cadenas en objetos ProtocolMessage
- **serialize()**: Convierte objetos ProtocolMessage en cadenas
- **Validadores**: Métodos para verificar tipos de mensaje

## 🔧 Funcionalidades

### Servidor
- Gestión de conexiones múltiples de clientes
- Enrutamiento de mensajes entre usuarios
- Manejo de sesiones de usuario
- Validación de protocolo

### Cliente
- Conexión segura al servidor
- Envío y recepción de mensajes en tiempo real
- Interfaz de usuario para interacción
- Gestión automática del protocolo

## 📦 Instalación y Uso

### Prerequisitos
- Java JDK 17 o superior
- IDE compatible con Java (IntelliJ IDEA, Eclipse, VS Code)

### Compilación
```bash
# Compilar el proyecto
javac -d bin src/com/CustomMessagingProtocol/**/*.java

# Ejecutar servidor
java -cp bin com.CustomMessagingProtocol.Server.MainServer

# Ejecutar cliente
java -cp bin com.CustomMessagingProtocol.Client.MainClient
```

### Ejemplo de Uso del Protocolo

```java
// Crear mensaje de login
String loginMsg = ProtocolParser.format("LOGIN", "12345", "Usuario1", "", "", "Conectándose...");

// Parsear mensaje recibido
ProtocolMessage message = ProtocolParser.toProtocolMessage(receivedString);

// Verificar tipo de mensaje
if (ProtocolParser.isPrivateMessage(message)) {
    System.out.println("Mensaje de: " + message.getFrom());
    System.out.println("Contenido: " + message.body());
}
```

## 🔐 Características del Protocolo

- **Formato Estructurado**: Sintaxis clara y parseable
- **Metadatos Extensibles**: Headers flexibles para información adicional
- **Validación Integrada**: Verificación automática de tipos de mensaje
- **Manejo de Campos Vacíos**: Gestión robusta de valores nulos o vacíos
- **Serialización Bidireccional**: Conversión eficiente entre objetos y strings

## 🛡️ Manejo de Errores

El protocolo incluye manejo robusto de:
- Campos vacíos o nulos (reemplazados por "Empty")
- Mensajes malformados
- Tipos de mensaje inválidos
- Conexiones perdidas

## 👥 Equipo de Desarrollo

| Nombre | Rol | Responsabilidad |
|--------|-----|-----------------|
| **Diego Alberto Sánchez Infante** | Desarrollador Backend y Frontend | Implementación del servidor y lógica de red |
| **Juan Carlos Mota González** | Desarrollador Backend y Frontend | Desarrollo de cliente genérico |
| **Javier Alejandro Hernández Servín** | Desarrollador Backend y Frontend | Implementación de cliente alternativo |
| **David Emmanuel Serrano Moreno** | Desarrollador Backend y Frontend | Desarrollo de cliente de usuario |

## 🔄 Flujo de Comunicación

1. **Conexión**: Cliente establece conexión TCP con el servidor
2. **Autenticación**: Cliente envía mensaje LOGIN con credenciales
3. **Sesión Activa**: Intercambio de mensajes PRIVATE_MESSAGE
4. **Desconexión**: Cliente envía LOGOUT antes de cerrar conexión

## 📈 Rendimiento

- **Protocolo Ligero**: Overhead mínimo en cada mensaje
- **Parsing Eficiente**: Operaciones de string optimizadas
- **Conexiones Concurrentes**: Soporte para múltiples clientes simultáneos
- **Gestión de Memoria**: Uso de records para inmutabilidad y eficiencia

## 🚀 Extensibilidad

El diseño modular permite fácil extensión:
- Nuevos tipos de mensaje en MessageType enum
- Headers adicionales en ProtocolMessage
- Validadores personalizados en ProtocolParser
- Nuevos formatos de serialización

---

⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub!
