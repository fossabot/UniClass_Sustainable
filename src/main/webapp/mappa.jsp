<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.unisa.uniclass.utenti.model.Utente, it.unisa.uniclass.utenti.model.Tipo" %>

<%
	/* Sessione HTTP */
	HttpSession sessione = request.getSession(true);
	Utente user = (Utente) sessione.getAttribute("currentSessionUser");

	if (user != null) {
		sessione.setAttribute("utenteEmail", user.getEmail());
	}

	Tipo tipoUtente = (user != null) ? (Tipo) user.getTipo() : null;
%>

<!DOCTYPE html>
<html lang="it">
<head>
	<title>Mappa UniSA</title>

	<meta name="viewport" content="width=device-width, initial-scale=1">

	<script src="scripts/sidebar.js" defer></script>

	<link rel="stylesheet" href="styles/headerStyle.css">
	<link rel="stylesheet" href="styles/barraNavigazioneStyle.css">
	<link rel="stylesheet" href="styles/mappa.css">
	<link rel="stylesheet" href="styles/footerstyle.css">

	<!-- Favicon WebP -->
	<link rel="icon" href="images/logois.webp" type="image/webp">
</head>

<body>

<!-- ================= BARRA NAVIGAZIONE ================= -->

<% if (tipoUtente == null) { %>
<div class="barraNavigazione" id="barraNavigazione">
	<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">
		<img src="images/icons/menuOpenIcon.webp" alt="Chiudi menu">
	</a>
	<p>Menu</p>
	<ul id="menu">
		<li><a href="aula.jsp">Aule</a></li>
		<li><a href="mappa.jsp">Mappa</a></li>
		<li><a href="ChatBot.jsp">ChatBot</a></li>
		<li><a href="infoapp.jsp">Info App</a></li>
		<li><a href="aboutus.jsp">Chi Siamo</a></li>
	</ul>
</div>

<% } else if (tipoUtente.equals(Tipo.Studente)) { %>
<div class="barraNavigazione" id="barraNavigazione">
	<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">
		<img src="images/icons/menuOpenIcon.webp" alt="Chiudi menu">
	</a>
	<p>Menu</p>
	<ul id="menu">
		<li><a href="aula.jsp">Aule</a></li>
		<li><a href="Conversazioni">Conversazioni</a></li>
		<li><a href="mappa.jsp">Mappa</a></li>
		<li><a href="ChatBot.jsp">ChatBot</a></li>
		<li><a href="infoapp.jsp">Info App</a></li>
		<li><a href="aboutus.jsp">Chi Siamo</a></li>
	</ul>
</div>

<% } else if (tipoUtente.equals(Tipo.Docente) || tipoUtente.equals(Tipo.Coordinatore)) { %>
<div class="barraNavigazione" id="barraNavigazione">
	<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">
		<img src="images/icons/menuOpenIcon.webp" alt="Chiudi menu">
	</a>
	<p>Menu</p>
	<ul id="menu">
		<li><a href="aula.jsp">Aule</a></li>
		<li><a href="Conversazioni">Conversazioni</a></li>
		<li><a href="mappa.jsp">Mappa</a></li>
		<li><a href="ChatBot.jsp">ChatBot</a></li>
		<li><a href="infoapp.jsp">Info App</a></li>
		<li><a href="aboutus.jsp">Chi Siamo</a></li>
	</ul>
</div>

<% } else if (tipoUtente.equals(Tipo.PersonaleTA)) { %>
<div class="barraNavigazione" id="barraNavigazione">
	<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">
		<img src="images/icons/menuOpenIcon.webp" alt="Chiudi menu">
	</a>
	<p>Menu</p>
	<ul id="menu">
		<li><a href="aula.jsp">Aule</a></li>
		<li><a href="PersonaleTA/AttivaUtenti.jsp">Gestione Utenti</a></li>
		<li><a href="mappa.jsp">Mappa</a></li>
		<li><a href="ChatBot.jsp">ChatBot</a></li>
		<li><a href="infoapp.jsp">Info App</a></li>
		<li><a href="aboutus.jsp">Chi Siamo</a></li>
	</ul>
</div>
<% } %>

<!-- ================= HEADER ================= -->
<jsp:include page="header.jsp"/>

<br><br>

<!-- ================= CONTENUTO ================= -->
<main>

	<section class="map-section">
		<h1>Mappa dell'Università degli Studi di Salerno</h1>

		<div class="map-container">

			<!-- MAPPA STATICA (WEBP) -->
			<img
					src="images/unisa-map-static.webp"
					alt="Mappa statica dell'Università degli Studi di Salerno"
					class="map"
					width="1000"
					height="700"
					loading="lazy">

			<!-- CARICAMENTO ON-DEMAND -->
			<button class="load-map-btn" onclick="loadInteractiveMap()">
				Apri mappa interattiva
			</button>

			<noscript>
				<p>
					JavaScript è disabilitato.
					<a href="https://www.google.com/maps/place/Università+degli+Studi+di+Salerno" target="_blank">
						Apri la mappa su Google Maps
					</a>
				</p>
			</noscript>

		</div>
	</section>

</main>

<!-- ================= FOOTER ================= -->
<%@ include file="footer.jsp" %>

<!-- ================= SCRIPT MAPPA ================= -->
<script>
	function loadInteractiveMap() {
		const container = document.querySelector(".map-container");

		container.innerHTML = `
        <iframe
            class="map"
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3024.0962216837215!2d14.7889441!3d40.7719627!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x133bc5dc8cfed6d7%3A0xfde22b53e7c5e9fc!2sUniversit%C3%A0%20degli%20Studi%20di%20Salerno!5e0!3m2!1sit!2sit!4v1704300000000!5m2!1sit!2sit"
            width="1000"
            height="700"
            style="border:0;"
            allowfullscreen=""
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade">
        </iframe>
    `;
	}
</script>

</body>
</html>
