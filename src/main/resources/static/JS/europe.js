const europeContinent = [
  {
    zone: "Central Europe",
    title: "Explorez le monde et partez à la découverte des montagnes.",
    src: "../static/IMAGES/europe_explore_europe.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 2315,
  },
  {
    zone: "South Europe",
    title: "Voyage en Espagne",
    src: "../static/IMAGES/europe_Espage_Barcelone.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 7846,
  },
  {
    zone: "Central Europe",
    title: "Mes endroits préféres à Paris",
    src: "../static/IMAGES/europe_Paris.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 12492,
  },
  {
    zone: "North Europe",
    title: "Londres",
    src: "../static/IMAGES/europe_Londres.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 1764,
  },
  {
    zone: "North Europe",
    title: "Londres - Explore le monde dès maintenant",
    src: "../static/IMAGES/europe_london_tour.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 9208,
  },
  {
    zone: "South Europe",
    title: "Venise - Partez avec nous pour de merveilleuses vacances !",
    src: "../static/IMAGES/europe_Italie_Venise.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 15678,
  },
  {
    zone: "South Europe",
    title: "J'aime l'Italie",
    src: "../static/IMAGES/europe_Italie_culinaire.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 3547,
  },
  {
    zone: "Central Europe",
    title: "Bienvenue à Paris",
    src: "../static/IMAGES/europe_France_Paris.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 10893,
  },
  {
    zone: "East Europe",
    title: "Voyage en Europe",
    src: "../static/IMAGES/europe_trip.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 4621,
  },
  {
    zone: "West Europe",
    title: "Europe - 11 Jours | 09 Nuits",
    src: "../static/IMAGES/europe_roadtrip.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 8702,
  },
  {
    zone: "South Europe",
    title: "Vlog - Hola amigos!",
    src: "../static/IMAGES/europe_Espagne.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 14385,
  },
  {
    zone: "West Europe",
    title: "Amsterdam - Explorer le monde maintenant",
    src: "../static/IMAGES/europe_amsterdam 2vegas.png",
    description: "Lorem, ipsum",
    autors: "Nova99",
    views: 6043,
  },
];

europeContinent.sort(function (a, b) {
  return b.views - a.views;
});

const container = document.getElementById("europe-container");

europeContinent.forEach((item) => {
  const card = document.createElement("figure");
  card.className = "video-list";

  card.innerHTML = `
      <img src="${item.src}" alt="${item.title}" class="card-image">
        <figcaption class="card-caption">
            <h4>${item.title}</h4>
            <span class="card-author">
              <img src="../static/IMAGES/avatar_profil.png" alt="logo_autor" class="author-avatar"/>
              <p><strong>${item.autors}</strong></p>
            </span>
            <p class="card-description">${item.description}</p>
            <p class="card-views">
              <span class="bg-views">
                <strong> ${item.views.toLocaleString()}</strong>
              </span>de vues</p>
        </figcaption>
  `;

  container.appendChild(card);
});
