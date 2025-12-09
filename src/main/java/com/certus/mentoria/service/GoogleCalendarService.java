package com.certus.mentoria.service;

import com.certus.mentoria.model.sesion.Sesion;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory; // <-- IMPORT CORRECTO
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import java.util.Date;
import java.time.ZoneId;

@Service
public class GoogleCalendarService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public GoogleCalendarService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    private static final String APPLICATION_NAME = "MentoriaApp";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public Calendar buildCalendarClient(String accessToken) throws Exception {
        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Método para crear el evento y enviar notificaciones
    private void crearEvento(Calendar calendar, Sesion sesion) throws Exception {

        Event event = new Event()
                .setSummary("Sesión con " + sesion.getAprendiz().getUsuario().getNombre())
                .setDescription("Sesión de mentoría confirmada");

        // Establecer horario
        Date inicio = Date.from(sesion.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
        Date fin = Date.from(sesion.getFechaHora().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());

        EventDateTime start = new EventDateTime().setDateTime(new DateTime(inicio)).setTimeZone("America/Lima");
        EventDateTime end = new EventDateTime().setDateTime(new DateTime(fin)).setTimeZone("America/Lima");
        event.setStart(start);
        event.setEnd(end);

        // Agregar invitados
        List<EventAttendee> attendees = List.of(
                new EventAttendee().setEmail(sesion.getAprendiz().getUsuario().getEmail()),
                new EventAttendee().setEmail(sesion.getMentor().getUsuario().getEmail()));
        event.setAttendees(attendees);

        // Insertar evento y enviar notificaciones
        calendar.events()
                .insert("primary", event)
                .setSendUpdates("all") // envía correos a todos los invitados
                .execute();
    }

    // Método público que se llama desde el controlador
    public void crearEventoSesion(Sesion sesion, DefaultOidcUser principal) throws Exception {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "google", // registrationId del proveedor
                principal.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        Calendar calendar = buildCalendarClient(accessToken);

        crearEvento(calendar, sesion);
    }
}
