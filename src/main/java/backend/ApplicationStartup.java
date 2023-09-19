package backend;

import backend.service.EntryService;
import backend.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationRunner {
    private EntryService entryService;
    private UserService userService;

    public ApplicationStartup(
            UserService userService,
            EntryService entryService
    ) {
        this.userService = userService;
        this.entryService = entryService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

//        this.entryService.createEntry();


    }
}
