function loadHeaderFooter() {
  fetch("header.html")
    .then((response) => response.text())
    .then((data) => {
      document.querySelector("header").innerHTML = data;

      // Ajouter le code pour le menu déroulant dans le header
      const dropdownHTML = `
        <li>
          <ul class="dropdown">
            <li><a href="africa.html">Afrique</a></li>
            <li><a href="europe.html">Europe</a></li>
            <li><a href="asia.html">Asie</a></li>
            <li><a href="america.html">Amérique</a></li>
            <li><a href="oceania.html">Océanie</a></li>
          </ul>
        </li>
      `;

      // Ajouter le menu déroulant au menu existant
      const navbar = document.querySelector(".navbar ul");
      navbar.insertAdjacentHTML("beforeend", dropdownHTML);
    });

  fetch("footer.html")
    .then((response) => response.text())
    .then((data) => {
      document.querySelector("footer").innerHTML = data;
    });
}

// Exécuter la fonction pour charger le header et le footer
document.addEventListener("DOMContentLoaded", loadHeaderFooter);

/*function myFunction() {
  const navMobile = document.getElementById("nav-mobile");
  navMobile.classList.toggle("show");
}

document.querySelectorAll('.nav-mobile > li > a').forEach(item => {
  item.addEventListener('click', function () {
    const dropdown = this.nextElementSibling;
    if (dropdown && dropdown.classList.contains('dropdown')) {
      dropdown.classList.toggle("show");
    }
  });
});*/
