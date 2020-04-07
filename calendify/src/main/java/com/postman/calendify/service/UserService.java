package com.postman.calendify.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postman.calendify.exception.CalendifyException;
import com.postman.calendify.models.Roles;
import com.postman.calendify.models.User;
import com.postman.calendify.repository.IUserRepository;

@Service
public class UserService {

	@Autowired
	private IUserRepository userRepo;

	public void save(User user, HttpSession httpSession) throws CalendifyException {
		if (authenticateUser(user)) {
			throw new CalendifyException(409, "User already exsits");
		}
		for (Roles role : Roles.values()) {
			if (role.getRole().equals(user.getRole())) {
				if (user.getPwd() == null || user.getUsername() == null || user.getFirstName() == null
						|| user.getPwd() == null) {
					throw new CalendifyException(422, "User missing one of the required fields");
				}
				user.setPwd(hash(user.getPwd()));
				userRepo.save(user);
				return;
			}
		}
		throw new CalendifyException(422, "Invalid User Role");
	}

	public boolean signin(User user, HttpSession httpSession) throws CalendifyException {
		if (!httpSession.isNew() && httpSession.getAttribute("username") != null) {
			String username = (String) httpSession.getAttribute("username");
			if (!username.equals(user.getUsername())) {
				throw new CalendifyException(409, "User already signed in");
			}
		}
		if (authenticateUser(user)) {
			httpSession.setAttribute("username", user.getUsername());
			httpSession.setAttribute("userid", user.getId());
			httpSession.setAttribute("userrole", user.getRole());
			return true;
		}
		throw new CalendifyException(401, "Username or Password is wrong");
	}

	public void signout(HttpSession httpSession) throws CalendifyException {
		if (httpSession.isNew() && httpSession.getAttribute("username") == null) {
			throw new CalendifyException(204, "No user is signed in");
		}
		httpSession.invalidate();
	}

	private boolean authenticateUser(User user) throws CalendifyException {
		User userFromRepo = userRepo.findByUsername(user.getUsername());
		if (userFromRepo != null) {
			user.setId(userFromRepo.getId());
			user.setRole(userFromRepo.getRole());
			;
		}
		return userFromRepo != null && userFromRepo.getUsername().equals(user.getUsername())
				&& userFromRepo.getPwd().equals(hash(user.getPwd()));
	}

	private String hash(String password) throws CalendifyException {
		StringBuilder hash = new StringBuilder();
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(password.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new CalendifyException(500, "Error while hashing passwords");
		}
		return hash.toString();
	}
}
