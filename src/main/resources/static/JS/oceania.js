const oceaniaContinent = [
  {
    zone: "Australia",
    title: "Voyage en Australie 4 jours et 5 nuits",
    src: "./images/oceanie_australie-1-venise.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 2315,
  },
  {
    zone: "Australia",
    title: "Vlog - Voyage en australie",
    src: "./images/oceanie_australie 2-culinaire.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 7846,
  },
  {
    zone: "Melanesia",
    title: "Lorem Ipsum",
    src: "./images/oceanie_Newzeland5.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 12492,
  },
  {
    zone: "Australia",
    title: "Calendrier des voyages en Australie",
    src: "./images/oceanie_Australien_tour_du_monde.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 1764,
  },
  {
    zone: "Australia",
    title: "Nouveauté - Voyage en Australie",
    src: "./images/oceanie_australie 6.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 9208,
  },
  {
    zone: "Australia",
    title: "Journée de l'Australie - mini voyage",
    src: "./images/oceanie_australie 7-favoris.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 15678,
  },
  {
    zone: "Melanesia",
    title: "Vlog - voyage en Mélanésie",
    src: "./images/oceanie_Melanesia 4.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 3547,
  },
  {
    zone: "Melanesia",
    title: "Découvrir la Mélanésie",
    src: "./images/oceanie_Melanesia.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 10893,
  },
  {
    zone: "New Zealand",
    title: "Tour de la Nouvelle-Zélande",
    src: "./images/oceanie_new zealand.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 4621,
  },
  {
    zone: "Melanesia",
    title: "Mélanésie - 11 jours et 9 nuits",
    src: "./images/oceanie_Melanesia2.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 8702,
  },
];

oceaniaContinent.sort(function (a, b) {
  return b.views - a.views;
});

const oceaniaContainer = document.getElementById("oceania-container");

oceaniaContinent.forEach((item) => {
  const card = document.createElement("figure");
  card.className = "video-list";

  card.innerHTML = `
      <img src="${item.src}" alt="${item.title}" class="card-image">
        <figcaption class="card-caption">
            <h4>${item.title}</h4>
            <span class="card-author">
              <img src="./icons/avatar_profil.png" alt="logo_autor" class="author-avatar"/>
              <p><strong>${item.autors}</strong></p>
            </span>
            <p class="card-description">${item.description}</p>
            <p class="card-views">
              <span class="bg-views">
                <strong> ${item.views.toLocaleString()}</strong>
              </span>de vues</p>
        </figcaption>
  `;

  oceaniaContainer.appendChild(card);
});
