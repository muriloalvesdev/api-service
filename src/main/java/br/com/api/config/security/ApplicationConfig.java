package br.com.api.config.security;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.api.domain.model.Role;
import br.com.api.domain.model.Role.RoleName;
import br.com.api.domain.repository.RoleRepository;

@Configuration
public class ApplicationConfig {

  private RoleRepository roleRepository;

  public ApplicationConfig(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Bean
  public void configVelocity() {
    Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    Velocity.setProperty("classpath.resource.loader.class",
        ClasspathResourceLoader.class.getName());
  }

  @Bean
  public void createRoleWhenInitializing() {
    for (RoleName roleName : RoleName.values()) {
      if (!roleRepository.findByName(roleName).isPresent()) {
        roleRepository.saveAndFlush(new Role(roleName));
      } else {
        continue;
      }
    }
  }
}
