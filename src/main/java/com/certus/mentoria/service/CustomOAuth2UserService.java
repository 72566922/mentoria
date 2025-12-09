package com.certus.mentoria.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.Rol;
import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.repository.PerfilAprendizRepository;
import com.certus.mentoria.repository.RolRepository;
import com.certus.mentoria.repository.UsuarioRepository;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PerfilAprendizRepository perfilRepo;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepo, RolRepository rolRepo,
            PerfilAprendizRepository perfilRepo) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.perfilRepo = perfilRepo;
    }

    @Transactional
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("✅✅✅ CustomOAuth2UserService EJECUTÁNDOSE ✅✅✅");

        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String nombre = oidcUser.getFullName();

        if (email == null) {
            throw new OAuth2AuthenticationException("Google no devolvió el email");
        }

        System.out.println("🟡 Usuario GOOGLE: " + email);

        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        // ✅ SI NO EXISTE → SE CREA
        if (usuario == null) {
            System.out.println("🟢 Usuario NO existe. Creando...");

            // Crear Usuario
            usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword("{noop}OAUTH");

            Rol rolAprendiz = rolRepo.findByNombre("APRENDIZ");
            if (rolAprendiz == null) {
                throw new OAuth2AuthenticationException(
                        "ERROR: El rol 'APRENDIZ' no existe en la base de datos");
            }
            usuario.setRoles(Set.of(rolAprendiz));

            usuarioRepo.save(usuario);
            System.out.println("✅ Usuario GOOGLE guardado correctamente en BD");

            // Crear PerfilAprendiz vacío asociado al Usuario
            PerfilAprendiz perfil = new PerfilAprendiz();
            perfil.setUsuario(usuario);
            perfil.setObjetivo("");
            perfil.setNivelAcademico("");
            perfil.setFotoPerfil(null);
            perfilRepo.save(perfil); // necesitas inyectar PerfilAprendizRepository
            System.out.println("✅ PerfilAprendiz creado para el nuevo usuario");
        } else {
            System.out.println("🔵 Usuario ya existe en BD");
        }

        // ✅ CONVERTIR ROLES A AUTHORITIES
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
        }

        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo());
    }
}
