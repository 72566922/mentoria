package com.certus.mentoria.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.certus.mentoria.model.user.Rol;
import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.repository.RolRepository;
import com.certus.mentoria.repository.UsuarioRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepo, RolRepository rolRepo) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Datos básicos que llegan desde Google
        String email = oAuth2User.getAttribute("email");
        String nombre = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new OAuth2AuthenticationException("Google no devolvió un email válido");
        }

        // Buscar si ya existe
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        // Si no existe → lo creamos
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNombre(nombre);
            usuario.setPassword(null); // No se usa con OAuth

            // Asignar rol por defecto
            Rol rolUsuario = rolRepo.findByNombre("ROLE_USER");
            if (rolUsuario == null) {
                throw new RuntimeException("ERROR: No existe el rol ROLE_USER en la base de datos");
            }

            Set<Rol> roles = new HashSet<>();
            roles.add(rolUsuario);
            usuario.setRoles(roles);

            usuarioRepo.save(usuario);
        }

        // Convertir los roles a autoridades de Spring Security
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
        }

        // Devolver usuario autenticado a Spring Security
        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "email" // atributo usado como identificador
        );
    }
}
