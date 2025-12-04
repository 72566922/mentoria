package com.certus.mentoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // buscar por email, no por username
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getEmail()) // login por email
                .password(usuario.getPassword())
                .authorities(
                    usuario.getRoles()
                            .stream()
                            .map(r -> "ROLE_" + r.getNombre())
                            .toArray(String[]::new)
                )
                .build();
    }
}
