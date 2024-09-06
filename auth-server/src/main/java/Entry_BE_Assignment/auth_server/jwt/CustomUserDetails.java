package Entry_BE_Assignment.auth_server.jwt;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import Entry_BE_Assignment.auth_server.entity.User;
import Entry_BE_Assignment.auth_server.enums.Role;
import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final User user;
	private final Role role;
	private final List<GrantedAuthority> authorities;

	public CustomUserDetails(User user) {
		this.user = user;
		this.role = user.getRole();
		this.authorities = List.of(() -> "ROLE_" + role);
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	public Long getUserId() {
		return user.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

