package com.lta.bancocanon.ServicioInventario.Usuario;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue   ;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data       //Agrega los getters y setters automaticamente
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity     //Para el uso del JPA
@Table (name = "Usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"nom_usuario"})})
public class Usuario implements UserDetails {
    
    @Id    
    @GeneratedValue 
    @Column(name = "id")
    Integer id;
    
    @Column(nullable = false)
    String nomUsuario;

    String contrasena;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    } 
    
    
    
    @Override
    public String getPassword() {
    return contrasena; 
}
    @Override
    public String getUsername() {
        return nomUsuario;
    }

}
