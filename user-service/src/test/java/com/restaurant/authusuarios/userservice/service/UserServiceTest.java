package com.restaurant.authusuarios.userservice.service;

import com.restaurant.authusuarios.userservice.client.AuthServiceClient;
import com.restaurant.authusuarios.userservice.dto.CrearUsuarioRequestDTO;
import com.restaurant.authusuarios.userservice.model.Rol;
import com.restaurant.authusuarios.userservice.model.UserProfile;
import com.restaurant.authusuarios.userservice.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
    private final AuthServiceClient authServiceClient = mock(AuthServiceClient.class);
    private final UserService userService = new UserService(userProfileRepository, authServiceClient);

    @Test
    void crear_whenEmailExists_shouldFail() {
        when(userProfileRepository.existsByEmailIgnoreCase("a@b.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                userService.crear(new CrearUsuarioRequestDTO("a@b.com", Rol.CLIENTE)));
    }

    @Test
    void crear_whenNewEmail_shouldReturnDto() {
        when(userProfileRepository.existsByEmailIgnoreCase("a@b.com")).thenReturn(false);
        when(userProfileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var dto = userService.crear(new CrearUsuarioRequestDTO("a@b.com", Rol.CLIENTE));

        assertEquals("a@b.com", dto.email());
        assertEquals(Rol.CLIENTE, dto.rol());
    }

    @Test
    void desactivar_shouldUpdateLocalAndNotifyAuthService() {
        var user = new UserProfile(1L, "a@b.com", Rol.CLIENTE, true, LocalDateTime.now());
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(authServiceClient).deactivateAccountByEmail("a@b.com");

        userService.desactivar(1L);

        verify(authServiceClient).deactivateAccountByEmail("a@b.com");
        assertEquals(false, user.getActivo());
    }
}
