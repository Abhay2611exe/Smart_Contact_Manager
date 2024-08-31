console.log("Script loaded");

let currentTheme = getTheme();
changeTheme();


function changeTheme() {
  document.querySelector("html").classList.add(currentTheme);

  const changeTheme = document.querySelector("#theme_change_button");
  

  changeTheme.addEventListener("click", (event) => {
    const oldTheme = currentTheme;

    if (currentTheme === "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }

    setTheme(currentTheme);
    document.querySelector("html").classList.remove(oldTheme);
    document.querySelector("html").classList.add(currentTheme);

    changeTheme.querySelector("span").textContent =
  currentTheme == "light" ? "Dark" : "Light";

  });
}

//Set theme to Localstorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

//Get theme from Localstorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  if (theme) return theme;
  else return "light";
}
