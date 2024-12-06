//Formulaire Modal
// Ajout d'un événement de clic au bouton pour afficher le formulaire
const btnOuvrir = document.getElementById("btnFormulaireInscription");
const formulaire = document.getElementById("inscription-form");
btnOuvrir?.addEventListener("click", function () {
  formulaire.style.display = "block";
  btnOuvrir.style.display = "none"; // Cache le bouton après avoir affiché le formulaire
});

const span = document.getElementById("close");
span?.addEventListener("click", function () {
  formulaire.style.display = "none"; // Masque le formulaire
  btnOuvrir.style.display = "block"; // Réaffiche le bouton pour rouvrir le formulaire
});

// Récupère la liste des utilisateurs et l'utilisateur actuellement connecté depuis le localStorage
const users = JSON.parse(localStorage.getItem("users")) || [];
let currentUser = JSON.parse(localStorage.getItem("currentUser")) || null;

// Affiche un message d'erreur
function afficherMessage(type, message) {
  const messageBox = document.getElementById("message-box");
  messageBox.className = `message ${type}`;
  messageBox.textContent = message;
  messageBox.style.display = "block";

  // Cache le message d'erreur après 3 secondes
  setTimeout(() => {
    messageBox.style.display = "none";
  }, 3000);
}

// Fonction d'inscription
function inscrireUtilisateur(username, password) {
  // Vérifie si un utilisateur avec le même nom d'utilisateur existe déjà
  if (users.some((user) => user.username === username)) {
    afficherMessage("error", "Nom d'utilisateur déjà pris !");
    return false;
  }
  // Ajoute le nouvel utilisateur
  users.push({ username, password, favoris: [] });
  localStorage.setItem("users", JSON.stringify(users));
  afficherMessage("success", "Compte créé avec succès !");
  return true;
}

// Fonction de connexion
function connecterUtilisateur(username, password) {
  const user = users.find(
    (user) => user.username === username && user.password === password
  );
  if (user) {
    currentUser = user;
    localStorage.setItem("currentUser", JSON.stringify(currentUser));
    afficherMessage("success", "Connexion réussie !");
    window.location.href = "/card"; // Redirection
  } else {
    afficherMessage("error", "Nom d'utilisateur ou mot de passe incorrect !");
  }
}

// Fonction de déconnexion
function deconnecterUtilisateur() {
  // Supprimer l'utilisateur actuel du localStorage
  localStorage.removeItem("currentUser");
  // Rediriger l'utilisateur vers la page de connexion
  window.location.href = "registre.html";
}

// Gestion des formulaires de connexion et d'inscription
document
  .getElementById("connexion-form")
  ?.addEventListener("submit", function (e) {
    e.preventDefault();
    const username = document.getElementById("login-username").value;
    const password = document.getElementById("login-password").value;
    connecterUtilisateur(username, password);
  });

document
  .getElementById("inscription-form")
  ?.addEventListener("submit", function (e) {
    e.preventDefault();
    const username = document.getElementById("signup-username").value;
    const password = document.getElementById("signup-password").value;
    if (inscrireUtilisateur(username, password)) {
      connecterUtilisateur(username, password);
    }
  });

document.addEventListener("DOMContentLoaded", () => {
  // Affiche ou masque le bouton de déconnexion
  const logoutBtn = document.getElementById("logout-btn");
  if (currentUser) {
    logoutBtn.style.display = "block";
  } else {
    logoutBtn.style.display = "none";
  }

  logoutBtn?.addEventListener("click", function () {
    console.log("déconnexion");

    deconnecterUtilisateur();
  });
});
