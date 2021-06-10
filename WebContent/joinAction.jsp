<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %> 
<%@ page import="java.io.PrintWriter"%> <!-- JS문장을 작성하기 위해 사용 -->
<% request.setCharacterEncoding("UTF-8"); %> <!--  건너오는 모든 data를 UTF-8으로 받을 수 있도록함 -->

<!--js bean 1명의 user정보를 담을 수 있는 bean, scope="page"현재 페이지에서만 사용도리 수 있게함-->
<jsp:useBean id ="user" class="user.User" scope="page"/> 
<jsp:setProperty name="user" property="userID"/>
<jsp:setProperty name="user" property="userPassword"/>
<jsp:setProperty name="user" property="userName" />
<jsp:setProperty name="user" property="userGender" />
<jsp:setProperty name="user" property="userEmail" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>  <!-- 로그인 회원들은 페이지에 접속할 수 없도록 -->  
	<%
				String userID = null;
				if (session.getAttribute("userID") != null){
					userID = (String) session.getAttribute("userID");
				}
				
				if (userID != null) {
					PrintWriter script = response.getWriter(); 
					script.println("<script>"); 
					script.println("alert('이미 로그인이 되어있습니다.')"); 
					script.println("location.href = 'main.jsp'");
					script.println("</script>");
				}
				
				if (user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null
				    || user.getUserGender() == null || user.getUserEmail() == null){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('입력이 안 된 사항이 있습니다.')");
					script.println("history.back()");
					script.println("</script>");
				} else{
				UserDAO userDAO = new UserDAO(); //인스턴스생성
				int result = userDAO.join(user); //join(user) 위에서와 같이 각각의 변수를 입력받아 만들어진 user라는 인스턴스가 join함수를 수행하도록 매개변수로 들어간다.
				
				if(result == -1){ // 아이디가 기본키기. 중복되면 오류.
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('이미 존재하는 아이디 입니다.')");
					script.println("history.back()");
					script.println("</script>");
				}
				//가입성공
				else {
					session.setAttribute("userID", user.getUserID()); 
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("location.href = 'main.jsp'");
					script.println("</script>");
				}
			}
	%>
</body>
</html>
