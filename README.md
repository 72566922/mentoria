Tener claro el acceso con Google OUTH

En caso mio, cree variable de entorno en mi equipo para las claves que me dieron:  
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}

Al primer momento de iniciar sesion con google, se registrara en la base de datos y tratara de crear un perfil en automatico de aprendiz, con datos insufucientes para que lo complete luego.
Luego del primer login y registro, el sistema solo validara de que ya exista para que en las futuras inicio de sesion ya no vuelva a registrarse en la base de datos y solo inicie sesion.

Para que se de inicio a la sesion basta con un solo aprendiz, siempre y cuando el mentor acepte o confirme la sesion.
