package data.jpa.domain.dto;

/**
 * 建造者模式 链式调用
 * 
 * @author Administrator
 *
 */
public class UserDTO {
	private String userId;
	private String username;
	private String nickname;
	private String birthday;

	private UserDTO(UserBuilder builder) {
		this.userId = builder.userId;
		this.username = builder.username;
		this.nickname = builder.userId;
		this.birthday = builder.birthday;
	}

	public static UserBuilder build() {
		return new UserBuilder();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "&" + (userId != null ? "userId=" + userId + "&" : "")
				+ (username != null ? "username=" + username + "&" : "")
				+ (nickname != null ? "nickname=" + nickname + "&" : "")
				+ (birthday != null ? "birthday=" + birthday : "");
	}

	public static class UserBuilder {
		private String userId;
		private String username;
		private String nickname;
		private String birthday;

		public UserBuilder buildUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public UserBuilder buildUsername(String username) {
			this.username = username;
			return this;
		}

		public UserBuilder buildNickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public UserBuilder buildBirthday(String birthday) {
			this.birthday = birthday;
			return this;
		}

		public UserDTO build() {
			return new UserDTO(this);
		}
	}

}
