# JWT

## ¿Que es?
- JSON Web Tokens
- Estandar abierto, RFC 7519 [Link](https://tools.ietf.org/html/rfc7519)
- Define una forma compacta y segura de la transmision de un objeto json
- Es un contenedor de informacion


## Partes
- Cada parte esta codificada en Base64 y separada por un punto

### JOSE
- JavaScript Object Signing and Encryption
- Define cual es la *tecnologia criptografica | sistema de encriptacion* que se va a aplicar al token

### Algoritmos criptograficos
- sda

```
# code block
{
"alg": "HS256", // Algoritmo criptografico a utilizar
"typ": "JWT" 	// Tipo de token
}
```

### Claims | PayLoad
- Almacena informacion del negocio

### Firma de seguridad
- Permite dar validez al token


- Permite representar claims de forma segura entre dos partes


jwt se puede signed usando un secret (alg hmac) o un par de llaves public/private utilizando rsa

-compacto: debido al tamaño puede ser enviada en una url(header http or post | max size: 2,083 characters) transmision rapida
-auto-contenido: el payload contiene la informacion requerida sobre el usuario, para evitar la consulta de la bd mas de una vez. carga toda la informacion necesaria dentro de si mismo(usuario y un signature).

cuando usarlo
-autenticacion: una vez el usuario este conectado, para cada solicitud subsiguiente incluira la jwt para poder acceder a los recursos
-intercambio de informacion: forma segura de transmitir informacion, por que a medida que se puede signed, usando claves public/private, usted puede estar seguro de que los remitentes son quienes dicen que son

estructura
-header: consta de 2 partes, estas luego se codifican en base64url
	*tipo de token
	*tipo de algoritmo hash
 {
   "alg": "HS256",
   "typ": "JWT"
 }
-payload: contiene los claims, estos contiene statements acerca de una entidad(user) y metadatos adicionales. hay 3 tipos de claims
	*reserved: son un conjunto de claims que no son obligatorios, pero se recomienda, que se creen para proporcionar un conjunto de utiles, algunos son: 
	        iss(issuer-emisor de la señal)
	        exp(fecha caducidad)
	        sub(subject del token)
	        aud(audiencia del token)
	        nbf(tiempo antes que la jwt no sea aceptada para procesar)
	        iat(tiempo jwt fue emitida, edad de la jwt)
	        jti(identificador unico para la jwt, evita la reproduccion)
	*public: pueden ser definidos por los que utilizan jwt, para evitar colisiones deben ser definidos en IANA JSON Web Token Registry or como un URI que contenga un namespace que resista las colisiones
	*private: estas son claims personaizadas para compartir informacion entre las partes que estan deacuerdo en usarlos

ej: {
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}

-signature: se utiliza para verificar que el emisor del jwt es quien dice que es, y para verificar que el mensaje no cambio en el camino
para crear el signature se tiene que tener el header codificado, la carga codificada, un secret, el algoritmo especifico en el header y el sign


uniendo todo
la salida es de 3 cadenas base64 separados por puntos
header.payload.signature
var encodedString = base64UrlEncode(header) + "." + base64UrlEncode(payload);
HMACSHA256(encodedString, 'secret');



funcionamiento
en la autenticacion un jwt es devuelto y se guarda localmente(puede ser como cookie), en lugar de crear una session y devolver una cookie
cada vez que el usuario quiera acceder a una ruta o recursos protegido, se debe enviar el jwt, tipicamente en el autorization header utilizando el esquema bearer, por lo tanto el contenido del header debe ser: Authorization: Bearer <token>

este es un mecanismo de autenticacion sin estado, por que el estado del usuario no se guarda en memoria del servidor, las rutas protegidas del servidor, se verificaran con jwt en el authorization header, y si es asi el usuario esta permitido, esto reduce la necesidad de ir hacia atras o adelante en la base de datos

browser>>>(post /users/login con las credenciales)>>>servidor(crea un jwt con un secret)
browser<<<(return l jwt al el browser)<<<servidor
browser>>>(envia el jwt con la authorization header)>>>servidor(valida el signature del jwt, obtiene la informacion del usuario desde el jwt)
browser<<<(envia un respuesta al cliente)<<<servidor


