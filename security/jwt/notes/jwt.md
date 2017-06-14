# JWT

## ¿Que es?
- JSON Web Tokens
- Estandar abierto, [RFC 7519](https://tools.ietf.org/html/rfc7519)
- Define una forma compacta y segura de la transmision de un objeto JSON
- Es un contenedor de informacion
- Permite validar la informacion enviada entre *aplicaciones | servicios* 

## Caracteristicas
- Es compacto, porque debe ser enviado en una *URL - Header - 2 083 caracteres*
- Debido a que es compacto su transmision es rapida  
- Autocontenido, al payload se le carga de informacion, para que esta a su vez, no requiera volver a ser solicitada a la base de datos 

## Partes
- Cada parte esta codificada en Base64 y separada por un punto
```
# Estructura
header. payload.signature
[base64UrlEncode(Header)].[base64UrlEncode(Payload)].[Hash]
```

### Header
- JOSE JavaScript Object Signing and Encryption
- Define cual es la *tecnologia criptografica | sistema de encriptacion* que se va a aplicar al token

#### Atributos
- **typ** tipo de token
- **cty*** tipo de contenido
- **alg** algoritmo a utilizar

### Algoritmos criptograficos

#### HS256
- Genera un hash para un texto utilizando una clave privada
- Solo pueden generarlo, aquellos que conocen la clave privada
- Permite saber que el contenido no ha sido modificado
- Permite saber quien es su creador
- HMAC, Es un hash con SHA256
- Permite utilizar una clave secreta que solo la conoce el creador del token
- Es un algoritmo simetrico, con una sola clave
- La clave es utilizada para generar la firma como para validarla
- Requiere que ambas partes conoscan la clave, si es que se quiere validar el destinatario/emisor en ambas partes

#### RS256
- RSA con SHA-256
- Requiere un par de llaves publicas y privadas
- Es un algoritmo asimetrico
- El proveedor de la entidad tiene una clave privada, usada para generar la firma
- El consumidor obtiene una llave publica 
- Solo requiere la llave publica, si es que se quiere validar el destinatario/emisor en ambas partes

```
# Ejemplo
{
"alg": "HS256", // Algoritmo criptografico a utilizar en la firma
"typ": "JWT" 	// Tipo de token
}
```

### PayLoad
- Almacena informacion del negocio y metadatos adicional propios del token

#### Claims

##### Reservados
: Son un conjunto de claims que no son obligatorios, pero se recomienda que se creen
- **iss**, creador - emisor de la señal, identifica quien creo el token
- **sub**, razon - razon de la generacion del token
- **aud**, audiencia - quien es el receptor, ej: web, android, ios. Requiere antes enviar el valor de esta propiedad
- **exp**, tiempo de expiracion - fecha de caducidad, sirve para verificar si el token esta vencido
- **nbf**, no antes - tiempo antes que el token no sea aceptado, desde que momento se va a comenzar a utilizar el token
- **iat**, creado - cuando fue creado el token, edad del token
- **jti**, id - identificador unico del token, esto evita la reproduccion

##### Publicos
- Pueden ser definidos por los que utilizan el token, para evitar colisiones deben ser definidos en IANA JSON Web Token Registry o como un URI que contenga un namespace que resista las colisiones

##### Privados
- Son claims personaizadas para compartir informacion entre las partes que estan deacuerdo en usarlos

```
# Ejemplo
{
"nombre": "Rodrigo"
}
```

### Signature
- Firma de seguridad
- Permite dar validez al token
- Se utiliza para verificar que el emisor del jwt es quien dice que es y para verificar que el mensaje no cambio en el camino
- Encripta el header y el payload utilizando un algoritmo


```
# Estructura HMAC
HMACSHA256(base64UrlEncode(Header)+"."+base64UrlEncode(Payload),Clave)
```

## Casos de uso
- Autenticacion
: Una vez el usuario este conectado, para cada solicitud subsiguiente incluira el token para poder acceder a los recursos, es un mecanismo de autenticacion sin estado por lo que el estado del usuario no se guarda en memoria del servidor, esto reduce la necesidad de ir hacia atras o adelante en la base de datos
- Intercambio de informacion
: Forma segura de transmitir informacion, por que a medida que se puede firmar, usando claves publicas o privadas, usted puede estar seguro de que los remitentes son quienes dicen que son


### Ejemplo de uso

#### Iniciar sesion
- Cliente inicia sesion en el servidor, envia su usuario y clave
- El sistema del servidor valida el usuario y clave, genera un token para esa sesion
- El cliente recepciona el token (si es web, puede guardarlo en una cookie o localstore, en lugar de crear una session)
- El cliente realiza peticiones al servidor, pero envia el token generado por este
- El servidor valida el token (decodifica en base64, con su clave privada valida el hash), si todo esta correcto retorna el recurso solicitado

#### Correo electronico
- Emisor añade su informacion al payload de un JWT
- Receptor valida que la informacion recibida no ha sido alterada

