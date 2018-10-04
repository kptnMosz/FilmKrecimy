package pl.wojtektrzos.filmkrecimy.service;

import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRepository;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@NoArgsConstructor
public class AvatarService {

    private final String AVATARS_PATH = "/home/coders/filmkrecimy_resources";
    private final String DEFAULT_AVATAR_NAME = "teddy.png";
    @Autowired
    private PlanItemRepository planItemRepository;

    private final String AVATAR_NAME = "avatar";

    public String updateAvatarPicture(File avatar, PlanItem owner) throws IOException {
        File avatarsHome = new File(AVATARS_PATH + File.separator + owner.getId() + File.separator);
        EnterLog.log("AvatarService", "Dodaje sprawdzam katalog", avatarsHome.getPath().toString());
        if (!avatarsHome.exists()) {
            avatarsHome.mkdir();
            EnterLog.log("AvatarService", "tworze katalog", avatarsHome.getPath().toString());
        }
        String avatarFileName = avatar.getName();
        File avatarFile = new File(avatarsHome.getPath() + File.separator + avatarFileName);
        if (avatarFile.exists()) {
            avatarFile.delete();
        }
        Files.move(Paths.get(avatar.getPath()), Paths.get(avatarFile.getPath()));
        owner.setFoto(avatarFile.getPath());
        planItemRepository.save(owner);
        return avatarsHome.getPath() + " " + avatarFile;
    }

    public File getAvatar(PlanItem owner) {
        String avatarPath = owner.getFoto();
        if(avatarPath == null){
            avatarPath = AVATARS_PATH  + File.separator+DEFAULT_AVATAR_NAME;
        }
        File avatar = new File(avatarPath);
        if(avatar.exists()){
            return avatar;
        }
        return null;
    }

    public void placeAvatarFotoInOutput(HttpServletResponse resp, HttpServletRequest request,PlanItem owner) throws IOException {


        File avatarFoto = getAvatar(owner);
        String mime = request.getServletContext().getMimeType(avatarFoto.getName());
        resp.setContentType(mime);
        resp.setContentLength((int) avatarFoto.length());
        FileInputStream in = new FileInputStream(avatarFoto);
        OutputStream out = resp.getOutputStream();

        // Copy the contents of the file to the output stream
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }
}
