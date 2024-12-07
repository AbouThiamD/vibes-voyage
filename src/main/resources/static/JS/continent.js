// Tableau des vidéos pour l'Asie
const asiaContinent = [
    { zone: "Southeast Asia", title: "Voyage à Dubai", src: "assets/image/photo-asie/dubai.png", authorAvatar: "assets/image/profil-photo-homme.png", autors: "Ivan Petrov", views: 4500 },
    { zone: "South Asia", title: "Voyage au Népal", src: "assets/image/photo-asie/asie1.jpg", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Anil Kumar", views: 12492 },
    { zone: "East Asia", title: "Tokyo - Explore Japan", src: "assets/image/photo-asie/JAPAN1.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Yuki Takahashi", views: 9847 },
    { zone: "South Asia", title: "Inde - À la découverte du Taj Mahal", src: "assets/image/photo-asie/india.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Raj Singh", views: 13247 },
    { zone: "Southeast Asia", title: "Plages de Malaisie", src: "assets/image/photo-asie/malaisia.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Somchai Phum", views: 7635 },
    { zone: "Central Asia", title: "Srilanka Découvrez Almaty", src: "assets/image/photo-asie/srilanka.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Aidar Zhakypov", views: 5372 },
    { zone: "East Asia", title: "Séoul - Une aventure moderne", src: "assets/image/photo-asie/korea.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Minji Kim", views: 11285 },
    { zone: "Southeast Asia", title: "Bali - Un havre de paix", src: "assets/image/photo-asie/INDONESIA.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Dewi Kartini", views: 15437 },
    { zone: "Central Asia", title: "Ouzbékistan - Samarcande", src: "assets/image/photo-asie/asie1.jpg", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Rustam Askarov", views: 8792 },
    { zone: "East Asia", title: "Pékin - Capitale chinoise", src: "assets/image/photo-asie/Chine.png", authorAvatar: "assets/image/photo-asie/profil-photo-homme.png", autors: "Li Wei", views: 4219 },
    { zone: "South Asia", title: "Sri Lanka - Temples et éléphants", src: "assets/image/photo-asie/asie.jpg", authorAvatar: "assets/image/photo-asie/avatar_sri_lanka.png", autors: "Nalini Perera", views: 8234 },
    { zone: "North Asia", title: "Korea - Les steppes infinies", src: "assets/image/photo-asie/korea2.png", authorAvatar: "assets/image/photo-asie/avatar_mongolia.png", autors: "Bataar Bold", views: 6231 }
];

// Trier les vidéos par nombre de vues (descendant)
asiaContinent.sort((a, b) => b.views - a.views);

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
    const popularVideos = asiaContinent.slice(0, 12); // Prendre les 12 vidéos les plus vues

    popularVideos.forEach((video) => {
        const card = createVideoCard(video);
        popularSection.appendChild(card);
    });
};

// Afficher les vidéos par catégorie géographique
const displayVideosByCategory = () => {
    const categories = {
        "North Asia": document.querySelector("#asie-nord .video-grid"),
        "South Asia": document.querySelector("#asie-sud .video-grid"),
        "East Asia": document.querySelector("#asie-est .video-grid"),
        "Southeast Asia": document.querySelector("#asie-ouest .video-grid"),
        "Central Asia": document.querySelector("#asie-ouest .video-grid")
    };

    asiaContinent.forEach((video) => {
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
