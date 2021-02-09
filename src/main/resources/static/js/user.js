let index = {
	init: function() {
		$("#btn-save").on("click", () => { // function(){} , => {} this를 바인딩 하기위해서 
			this.save();
		});
	},

	save: function() {
		//alert("user의 save함수 호출됨");
		let data ={
			username:$("#username").val(),
			password:$("#password").val(),
			email:$("#email").val()
		};
		
		
		//console.log(data);
		//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert요청을 할것이다
		//ajax 호출시 default가 비동기 호출
		// ajax 가 통신을 성공하고 나서 json을 리턴해주면 서버가 자동으로 자바오브젝트로 변환해준다
		$.ajax({
			type: "POST",
			url: "/blog/api/user",
			data: JSON.stringify(data), // 자바스크립트라서 잘모른다 JSON으로 변환해줘야 한다
			contentType: "application/json; charset=utf-8", //body데이터가 어떤 타입인지
			dataType: "json" // 요청을 서버로 해서 응답이 왔을 때, 모든것이 버퍼여서 문자열로 온다(생긴게 json이라면) => 자바스크립트 오브젝트로 변경 
		}).done(function(resp){
			alert("회원가입이 완료되었습니다.");
			location.href= "/blog";
			//
		}).fail(function(error){
			alert(JSON.stringify(error));
			//
		}); 
		
	},
	
		login: function() {
		//alert("user의 save함수 호출됨");
		let data ={
			username:$("#username").val(),
			password:$("#password").val(),
		};
		
		
	
		$.ajax({
			type: "POST",
			url: "/blog/api/user/login",
			data: JSON.stringify(data), // 자바스크립트라서 잘모른다 JSON으로 변환해줘야 한다
			contentType: "application/json; charset=utf-8", //body데이터가 어떤 타입인지
			dataType: "json" // 요청을 서버로 해서 응답이 왔을 때, 모든것이 버퍼여서 문자열로 온다(생긴게 json이라면) => 자바스크립트 오브젝트로 변경 
		}).done(function(resp){
			alert("로그인이 완료되었습니다.");
			location.href= "/blog";
			//
		}).fail(function(error){
			alert(JSON.stringify(error));
			//
		}); 
		
	}

}



index.init();