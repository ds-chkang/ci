package medousa.test;


import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class gitRepository {
    public static void main(String[] args) throws GitAPIException {
        String remoteUrl = "https://github.com/medousa-net/medousa.git";
        File localPath = new File("c:\\temp");

        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(remoteUrl);
        cloneCommand.setDirectory(localPath);
        cloneCommand.call();
    }
}