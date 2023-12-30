export function validateEmail(email: string) {
  var re = /^[\w.-]+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
  return re.test(email);
}

export function validateUsername(username: string) {
  var re = /^[a-zA-Z0-9]{3,20}$/;
  return re.test(username);
}

export function validatePassword(password: string) {
  var re =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  return re.test(password);
}

export function validateConfirmPassword(
  confirmPassword: string,
  password: string
) {
  return confirmPassword === password;
}
