package com.innoventes.jukebox.controller.helper;

import com.innoventes.jukebox.models.entity.AbstractUser;
import com.innoventes.jukebox.models.entity.FileStore;
import com.innoventes.jukebox.models.request.UpdateProfileRequest;
import com.innoventes.jukebox.models.response.ErrorResponse;
import com.innoventes.jukebox.models.response.FileResponse;
import com.innoventes.jukebox.models.response.JukeboxResponse;
import com.innoventes.jukebox.models.response.SuccessResponse;
import com.innoventes.jukebox.security.jwt.JwtUtils;
import com.innoventes.jukebox.service.StorageService;
import com.innoventes.jukebox.service.UserService;
import com.innoventes.jukebox.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.util.Optional;

@Component
public class UserHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private StorageService storageService;

    @Value("${jukebox.file.upload.path:/uploads/}")
    private String uploadPath;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<JukeboxResponse> updateProfileDetails(UpdateProfileRequest request) {
        LOGGER.debug("Update profile request received :\n" + CommonUtils.convertToJson(request));
        return ResponseEntity.ok(new SuccessResponse(userService.updateUser(request)));
    }

    public ResponseEntity<JukeboxResponse> fetchProfileDetails(String authHeader) {
        String jwtToken = CommonUtils.getJwtTokenFromAuthHeader(authHeader);
        String userEmail = jwtUtils.getSubjectFromJwtToken(jwtToken);
        LOGGER.debug("Fetch profile detail request received : Email - " + userEmail);
        AbstractUser user = userService.findUserByEmail(userEmail);
        return ResponseEntity.ok(new SuccessResponse(user));
    }

    public ResponseEntity<JukeboxResponse> updateProfilePic(MultipartFile file, String authHeader) {
        String jwtToken = CommonUtils.getJwtTokenFromAuthHeader(authHeader);
        String userEmail = jwtUtils.getSubjectFromJwtToken(jwtToken);
        LOGGER.debug("Update profile picture request received : Email - " + userEmail);
        AbstractUser user = userService.findUserByEmail(userEmail);
        userService.updateProfilePic(file, user);
        return ResponseEntity.ok(new SuccessResponse("Profile pic updated successfully!"));
    }

//    public ResponseEntity<JukeboxResponse> getProfilePic(String authHeader) {
//
//        String jwtToken = CommonUtils.getJwtTokenFromAuthHeader(authHeader);
//        String userEmail = jwtUtils.getSubjectFromJwtToken(jwtToken);
//        LOGGER.debug("Get profile picture request received : Email - " + userEmail);
//        Optional<AbstractUser> userOptional = userService.findUserByEmail(userEmail);
//        if (userOptional.isPresent()) {
//            Optional<FileStore> profilePicOptional = userService.getProfilePic(userOptional.get());
//            if (profilePicOptional.isPresent()){
//                FileStore profilePic = profilePicOptional.get();
//                String fileName = profilePic.getName();
//                Resource resource;
//                String contentType = null;
//                try {
//                    resource = storageService.loadFileAsResource(fileName);
//                    contentType =
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            return profilePic
//                    .<ResponseEntity<JukeboxResponse>>map(
//                            fileStore -> ResponseEntity.ok(new SuccessResponse(
//                                    new FileResponse(uploadPath + fileStore.getName(), fileStore.getType())))
//                    ).orElseGet(
//                            () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Profile pic not found"))
//                    );
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
//        }
//    }
}
