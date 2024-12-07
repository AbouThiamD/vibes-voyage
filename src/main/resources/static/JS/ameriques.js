// Tableau des vidéos pour l'Amérique
const ameriqueContinent = [
    { zone: "Amérique du Nord", title: "Voyage à New York yes", src_video:"./VIDEOS/amerique.mp4",  src: "./IMAGES/amerique/New york.png", authorAvatar: "./IMAGES/photo.png", autors: "John Doe", views: 10500 },
    { zone: "Amérique Latine", title: "Rio - Le carnaval du Brésil", src_video:"assets/video/chinese-tourist.mp4", src: "/assets/image/amerique/brazil 2.png", authorAvatar: "/assets/image/photo.png", autors: "Carlos Gomez", views: 8320 },
    { zone: "Amérique du Nord", title: "Aventure à Montréal", src_video:"assets/video/chinese-tourist.mp4", src: "/assets/image/amerique/canada2.png", authorAvatar: "/assets/image/photo.png", autors: "Marie Lemoine", views: 6521 },
    { zone: "Les îles", title: "Peru", src: "/assets/image/amerique/harvard.png", authorAvatar: "/assets/image/photo.png", autors: "David Smith", views: 4875 },
    { zone: "Amérique Latine", title: "Perou - Machu Picchu", src: "/assets/image/amerique/lav vegas.png", authorAvatar: "/assets/image/photo.png", autors: "Lucia Ramiro", views: 13547 },
    { zone: "Les îles", title: "Îles Galápagos - Un voyage unique", src: "/assets/image/amerique/lav vegas.png", authorAvatar: "/assets/image/photo.png", autors: "Alexis Pires", views: 9643 },
    { zone: "Amérique du Nord", title: "Voyage à New York", src: "/assets/image/amerique/new york2.png", authorAvatar: "/assets/image/photo.png", autors: "John Doe", views: 10500 },
    { zone: "Amérique Latine", title: "Rio - Le carnaval du Brésil", src: "assets/image/amerique/rio.png", authorAvatar: "/assets/image/photo.png", autors: "Carlos Gomez", views: 8320 },
    { zone: "Amérique du Nord", title: "Aventure à Montréal", src: "/assets/image/amerique/los angeles 2.png", authorAvatar: "/assets/image/photo.png", autors: "Marie Lemoine", views: 6521 },
    { zone: "Les îles", title: "Les plages des Bahamas", src: "/assets/image/amerique/canada2.png", authorAvatar: "/assets/image/photo.png", autors: "David Smith", views: 4875 },
    { zone: "Amérique Latine", title: "Perou - Machu Picchu", src: "/assets/image/amerique/hollywood.png", authorAvatar: "/assets/image/photo.png", autors: "Lucia Ramiro", views: 13547 },
    { zone: "Les îles", title: "Îles Galápagos - Un voyage unique", src: "/assets/image/amerique/los angeles 2.png", authorAvatar: "/assets/image/photo.png", autors: "Alexis Pires", views: 9643 },
    { zone: "Amérique du Nord", title: "Voyage à New York", src: "/assets/image/photo.png", autors: "John Doe", views: 10500 },

]; // ajouter src_video avec le lien

// Trier les vidéos par nombre de vues (descendant)
ameriqueContinent.sort((a, b) => b.views - a.views);

// Fonction pour générer une carte vidéo
const createVideoCard = (video) => {
    const card = document.createElement("figure");
    card.className = "video-list";

    card.innerHTML = `
        
        <a href="/pageVideos?video=${video.src_video}">
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
        </a>
    `;
    return card;
};

// Afficher les vidéos populaires (maximum 12)
const displayPopularVideos = () => {
    const popularSection = document.querySelector("#v-populaire .video-grid");
    const popularVideos = ameriqueContinent.slice(0, 12); // Prendre les 12 vidéos les plus vues

    popularVideos.forEach((video) => {
        const card = createVideoCard(video);
        popularSection.appendChild(card);
    });
};

// Afficher les vidéos par catégorie géographique
const displayVideosByCategory = () => {
    const categories = {
        "Amérique du Nord": document.querySelector("#amerique-nord .video-grid"),
        "Amérique Latine": document.querySelector("#amerique-latine .video-grid"),
        "Les îles": document.querySelector("#iles .video-grid"),
    };

    ameriqueContinent.forEach((video) => {
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
