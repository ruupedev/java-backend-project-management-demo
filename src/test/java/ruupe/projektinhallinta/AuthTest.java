package ruupe.projektinhallinta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;



@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unAuthUserCannotAccess() throws Exception {
        mockMvc.perform(get("/createProject"))
        .andExpect(status().isUnauthorized());
    }

    @Test
    void adminCasAccessCreateProject() throws Exception {
        mockMvc.perform(get("/createProject")
        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
        .andExpect(status().isOk());
    }

    @Test
    void userCannotAccessCreateProject() throws Exception {
        mockMvc.perform(get("/createProject")
        .with(user("user").authorities(new SimpleGrantedAuthority("USER"))))
        .andExpect(status().isForbidden());
    }
}
