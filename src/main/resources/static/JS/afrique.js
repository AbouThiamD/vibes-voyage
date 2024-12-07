// Tableau des vidéos pour l'Afrique
const africaContinent = [
    { zone: "Afrique du Nord", title: "Mystères des Pyramides - Égypte", src: "/assets/image/photo-afrique/chameaux.jpg", authorAvatar: "assets/image/photo.png", autors: "Ali Mansour", views: 15234 },
    { zone: "Afrique de l'Ouest", title: "Festival au Bénin - Voyage Culturel", src: "/assets/image/photo-afrique/Abidjan.png", authorAvatar: "assets/image/photo.png", autors: "Amina Diop", views: 10342 },
    { zone: "Afrique de l'Est", title: "Safari en Tanzanie - Aventure Nature", src: "/assets/image/photo-afrique/desert.jpg", authorAvatar: "assets/image/photo.png", autors: "Juma Kasongo", views: 9784 },
    { zone: "Afrique du Sud", title: "Les Grands Lacs de l'Afrique du Sud", src: "/assets/image/photo-afrique/meditation-afriquedusud.png", authorAvatar: "assets/image/photo.png", autors: "Pieter du Toit", views: 12675 },
    { zone: "Afrique du Nord", title: "Les ruines de Carthage - Tunisie", src: "/assets/image/photo-afrique/streetfoodtunisie.png", authorAvatar: "assets/image/photo.png", autors: "Karim Zayed", views: 11234 },
    { zone: "Afrique de l'Ouest", title: "Les plages de Côte d'Ivoire", src: "/assets/image/photo-afrique/mangerG.jpg", authorAvatar: "assets/image/photo.png", autors: "Fatou Sow", views: 14560 },
    { zone: "Afrique de l'Est", title: "Les monts du Kilimandjaro", src: "/assets/image/photo-afrique/meditation-afriquedusud.png", authorAvatar: "assets/image/photo.png", autors: "Nia Mwangi", views: 13478 },
    { zone: "Afrique du Sud", title: "Exploration de la ville du Cap", src: "/assets/image/photo-afrique/sportextremeafrique.png", authorAvatar: "assets/image/photo.png", autors: "Johan Van Der Merwe", views: 9903 },
    { zone: "Afrique du Nord", title: "Le désert du Sahara - Maroc", src: "/assets/image/photo-afrique/safarie-afrique.png", authorAvatar: "assets/image/photo.png", autors: "Sofia Ahmed", views: 12467 },
    { zone: "Afrique de l'Ouest", title: "Lagos - La ville dynamique DKR", src: "/assets/image/photo-afrique/vlogdkr.png", authorAvatar: "assets/image/photo.png", autors: "Tunde Akinsola", views: 11540 },
    { zone: "Afrique de l'Est", title: "Zanzibar - Îles tropicales en Tanzanie", src: "/assets/image/photo-afrique/vlog-maurice.png", authorAvatar: "assets/image/photo.png", autors: "Salim Ali", views: 15560 },
    { zone: "Afrique du Sud", title: "La faune sauvage de Kruger Park", src: "/assets/image/photo-afrique/surfàdkr.png", authorAvatar: "assets/image/photo.png", autors: "Megan Walker", views: 10322 },
];

// Trier les vidéos par nombre de vues (descendant)
africaContinent.sort((a, b) => b.views - a.views);

// Fonction pour générer une carte vidéo
const createVideoCard = (video) => {
    const card = document.createElement("figure");
    card.className = "video-list";

    card.innerHTML = `
        <img src="${video.src}" alt="${video.title}" class="card-image">
        <figcaption class="card-caption">
            <h4>${video.title}</h4>
            <span class="card-author">
                <img src="${video.authorAvatar}" alt="Photo de ${video.autors}" class="author-avatar">
                <p><strong>${video.autors}</strong></p>
            </span>
            <p class="card-views">
                <span class="bg-views">
                    <strong>${video.views.toLocaleString()}</strong> vues
                </span>
            </p>
        </figcaption>
    `;
    return card;
};

// Afficher les vidéos populaires (maximum 12)
const displayPopularVideos = () => {
    const popularSection = document.querySelector("#v-populaire .video-grid");
    const popularVideos = africaContinent.slice(0, 12); // Prendre les 12 vidéos les plus vues

    popularVideos.forEach((video) => {
        const card = createVideoCard(video);
        popularSection.appendChild(card);
    });
};

// Afficher les vidéos par catégorie géographique
const displayVideosByCategory = () => {
    const categories = {
        "Afrique du Nord": document.querySelector("#afrique-nord .video-grid"),
        "Afrique de l'Ouest": document.querySelector("#afrique-ouest .video-grid"),
        "Afrique de l'Est": document.querySelector("#afrique-est .video-grid"),
        "Afrique du Sud": document.querySelector("#afrique-sud .video-grid")
    };

    africaContinent.forEach((video) => {
        const categoryGrid = categories[video.zone];
        if (categoryGrid) {
            const card = createVideoCard(video);
            categoryGrid.appendChild(card);
        }
    });
};

// Initialisation : charger les vidéos
displayPopularVideos();
displayVideosByCategory();
