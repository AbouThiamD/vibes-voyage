// Récupérer le paramètre 'video' de l'URL
    const params = new URLSearchParams(window.location.search);
    const videoSrc = params.get('video');

    // Si une vidéo est spécifiée, mettre à jour la source
    if (videoSrc) {
    const videoSource = document.getElementById('video-source');
    videoSource.src = `${videoSrc}`; // Modifier selon votre structure de fichiers
    document.getElementById('video-player').load();
} else 
{
        alert("Aucune vidéo sélectionnée !");
}