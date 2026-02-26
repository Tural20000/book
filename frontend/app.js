const API_BASE =
  window.location.origin && window.location.origin.includes("8020")
    ? ""
    : "http://localhost:8020";

function getJwt() {
  return window.localStorage.getItem("jwt") || "";
}

function setTokens(jwt, refreshToken) {
  window.localStorage.setItem("jwt", jwt || "");
  window.localStorage.setItem("refreshToken", refreshToken || "");
}

function clearTokens() {
  window.localStorage.removeItem("jwt");
  window.localStorage.removeItem("refreshToken");
}

async function apiRequest(path, options = {}) {
  const headers = options.headers || {};
  if (!headers["Content-Type"] && !(options.body instanceof FormData)) {
    headers["Content-Type"] = "application/json";
  }
  const jwt = getJwt();
  if (jwt) {
    headers["Authorization"] = `Bearer ${jwt}`;
  }
  const resp = await fetch(API_BASE + path, {
    ...options,
    headers,
  });
  const isJson = resp.headers.get("content-type")?.includes("application/json");
  const data = isJson ? await resp.json().catch(() => null) : await resp.text();
  if (!resp.ok) {
    const error = new Error(
      typeof data === "string" && data ? data : "Xəta baş verdi"
    );
    error.status = resp.status;
    error.data = data;
    throw error;
  }
  return data;
}

function showElement(el) {
  el.classList.remove("hidden");
}

function hideElement(el) {
  el.classList.add("hidden");
}

function switchAuthTab(tab) {
  const tabRegister = document.getElementById("tab-register");
  const tabLogin = document.getElementById("tab-login");
  const registerForm = document.getElementById("register-form");
  const loginForm = document.getElementById("login-form");

  if (tab === "register") {
    tabRegister.classList.add("active");
    tabLogin.classList.remove("active");
    registerForm.classList.add("active");
    loginForm.classList.remove("active");
  } else {
    tabLogin.classList.add("active");
    tabRegister.classList.remove("active");
    loginForm.classList.add("active");
    registerForm.classList.remove("active");
  }
}

async function loadBooks() {
  const tbody = document.querySelector("#books-table tbody");
  tbody.innerHTML = "";

  try {
    const books = await apiRequest("/api/books", {
      method: "GET",
    });
    const template = document.getElementById("book-row-template");

    books.forEach((book) => {
      const rowFragment = template.content.cloneNode(true);
      const tr = rowFragment.querySelector("tr");
      tr.dataset.id = book.id;
      tr.querySelector(".cell-id").textContent = book.id;
      tr.querySelector(".cell-name").textContent = book.name;
      tr.querySelector(".cell-author").textContent = book.author;
      tr.querySelector(".cell-price").textContent = book.price;
      tr.querySelector(".cell-quantity").textContent = book.quantity;
      tbody.appendChild(rowFragment);
    });
  } catch (err) {
    console.error(err);
    alert("Kitabları yükləmək mümkün olmadı: " + err.message);
  }
}

function fillBookFormFromRow(tr) {
  const id = tr.dataset.id;
  const name = tr.querySelector(".cell-name").textContent;
  const author = tr.querySelector(".cell-author").textContent;
  const price = tr.querySelector(".cell-price").textContent;
  const quantity = tr.querySelector(".cell-quantity").textContent;

  document.getElementById("book-id").value = id;
  document.getElementById("book-name").value = name;
  document.getElementById("book-author").value = author;
  document.getElementById("book-price").value = price;
  document.getElementById("book-quantity").value = quantity;

  document.getElementById("book-save-btn").textContent = "Yenilə";
  showElement(document.getElementById("book-cancel-edit-btn"));
}

function resetBookForm() {
  document.getElementById("book-id").value = "";
  document.getElementById("book-name").value = "";
  document.getElementById("book-author").value = "";
  document.getElementById("book-price").value = "";
  document.getElementById("book-quantity").value = "";
  document.getElementById("book-save-btn").textContent = "Əlavə et";
  hideElement(document.getElementById("book-cancel-edit-btn"));
  const msg = document.getElementById("book-form-message");
  msg.textContent = "";
  msg.classList.remove("success");
}

function showBooksUI() {
  hideElement(document.getElementById("auth-section"));
  showElement(document.getElementById("books-section"));
  loadBooks();
}

function showAuthUI() {
  clearTokens();
  showElement(document.getElementById("auth-section"));
  hideElement(document.getElementById("books-section"));
}

window.addEventListener("DOMContentLoaded", () => {
  const tabRegisterBtn = document.getElementById("tab-register");
  const tabLoginBtn = document.getElementById("tab-login");
  const registerForm = document.getElementById("register-form");
  const loginForm = document.getElementById("login-form");
  const logoutBtn = document.getElementById("logout-btn");
  const refreshBtn = document.getElementById("refresh-btn");
  const bookForm = document.getElementById("book-form");
  const bookCancelEditBtn = document.getElementById("book-cancel-edit-btn");
  const booksTbody = document.querySelector("#books-table tbody");

  if (getJwt()) {
    showBooksUI();
  } else {
    showAuthUI();
  }

  tabRegisterBtn.addEventListener("click", () => switchAuthTab("register"));
  tabLoginBtn.addEventListener("click", () => switchAuthTab("login"));

  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("register-username").value.trim();
    const password = document.getElementById("register-password").value.trim();
    const msg = document.getElementById("register-message");
    msg.textContent = "";
    msg.classList.remove("success");

    if (!username || !password) {
      msg.textContent = "Username və password məcburidir.";
      return;
    }

    try {
      const respText = await apiRequest("/reg-user", {
        method: "POST",
        body: JSON.stringify({ username, password }),
      });
      msg.textContent = typeof respText === "string" ? respText : "Qeydiyyat uğurludur.";
      msg.classList.add("success");
      document.getElementById("register-password").value = "";
      switchAuthTab("login");
      document.getElementById("login-username").value = username;
    } catch (err) {
      msg.textContent = err.message || "Qeydiyyat zamanı xəta baş verdi.";
    }
  });

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("login-username").value.trim();
    const password = document.getElementById("login-password").value.trim();
    const msg = document.getElementById("login-message");
    msg.textContent = "";
    msg.classList.remove("success");

    if (!username || !password) {
      msg.textContent = "Username və password məcburidir.";
      return;
    }

    try {
      const data = await apiRequest("/apis/login", {
        method: "POST",
        body: JSON.stringify({ username, password }),
      });
      if (data && data.jwt) {
        setTokens(data.jwt, data.refreshToken || "");
        msg.textContent = "Uğurla daxil oldunuz.";
        msg.classList.add("success");
        document.getElementById("login-password").value = "";
        showBooksUI();
      } else {
        msg.textContent = "Gözlənilməyən cavab.";
      }
    } catch (err) {
      msg.textContent =
        err.status === 401
          ? "Username və ya password yanlışdır."
          : err.message || "Daxil olma zamanı xəta baş verdi.";
    }
  });

  logoutBtn.addEventListener("click", () => {
    showAuthUI();
  });

  refreshBtn.addEventListener("click", () => {
    loadBooks();
  });

  bookForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("book-id").value;
    const name = document.getElementById("book-name").value.trim();
    const author = document.getElementById("book-author").value.trim();
    const price = parseFloat(document.getElementById("book-price").value);
    const quantity = parseInt(document.getElementById("book-quantity").value, 10);
    const msg = document.getElementById("book-form-message");
    msg.textContent = "";
    msg.classList.remove("success");

    if (!name || !author || isNaN(price) || isNaN(quantity)) {
      msg.textContent = "Bütün kitab sahələri məcburidir.";
      return;
    }

    const payload = { name, author, price, quantity };

    try {
      if (id) {
        await apiRequest(`/api/books/${id}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
        msg.textContent = "Kitab uğurla yeniləndi.";
      } else {
        await apiRequest("/api/books", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        msg.textContent = "Kitab uğurla əlavə olundu.";
      }
      msg.classList.add("success");
      resetBookForm();
      loadBooks();
    } catch (err) {
      msg.textContent = err.message || "Əməliyyat zamanı xəta baş verdi.";
    }
  });

  bookCancelEditBtn.addEventListener("click", () => {
    resetBookForm();
  });

  booksTbody.addEventListener("click", async (e) => {
    const editBtn = e.target.closest(".edit-btn");
    const deleteBtn = e.target.closest(".delete-btn");
    if (!editBtn && !deleteBtn) {
      return;
    }
    const tr = e.target.closest("tr");
    const id = tr?.dataset.id;
    if (!id) return;

    if (editBtn) {
      fillBookFormFromRow(tr);
    } else if (deleteBtn) {
      if (!confirm("Bu kitabı silmək istədiyinizə əminsiniz?")) return;
      try {
        await apiRequest(`/api/books/${id}`, { method: "DELETE" });
        tr.remove();
      } catch (err) {
        alert(err.message || "Kitabı silmək mümkün olmadı.");
      }
    }
  });
});

