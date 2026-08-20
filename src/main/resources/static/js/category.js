"use strict";

const API_URL = "/api/categories";

const form = document.querySelector("#categoryForm");
const categoryIdInput = document.querySelector("#categoryId");
const nameInput = document.querySelector("#name");
const tableBody = document.querySelector("#categoryTableBody");
const messageElement = document.querySelector("#message");
const submitBtn = document.querySelector("#submitBtn");

async function loadCategories() {
  try {
    const response = await fetch(API_URL);

    if (!response.ok) {
      showMessage("Ангиллуудыг татахад алдаа гарлаа", "error");
      return;
    }

    const categories = await response.json();
    renderCategories(categories);
  } catch (error) {
    console.error("Fetch error:", error);
    showMessage("Сервертэй холбогдоход алдаа гарлаа", "error");
  }
}

function renderCategories(categories) {
  if (!tableBody) return;
  tableBody.innerHTML = "";

  if (categories.length === 0) {
    const emptyRow = document.createElement("tr");
    emptyRow.innerHTML = `<td colspan="3" style="text-align: center;">Дата олдсонгүй.</td>`;
    tableBody.appendChild(emptyRow);
    return;
  }

  categories.forEach((category) => {
    const row = document.createElement("tr");

    const idCell = document.createElement("td");
    idCell.textContent = category.id;

    const nameCell = document.createElement("td");
    nameCell.textContent = category.name;

    const actionCell = document.createElement("td");

    const editButton = document.createElement("button");
    editButton.textContent = "Засах";
    editButton.classList.add("btn", "btn-edit");
    editButton.addEventListener("click", () => {
      if (categoryIdInput) categoryIdInput.value = category.id;
      if (nameInput) nameInput.value = category.name;
      if (submitBtn) submitBtn.textContent = "Шинэчлэх";
    });

    const deleteButton = document.createElement("button");
    deleteButton.textContent = "Устгах";
    deleteButton.classList.add("btn", "btn-delete");
    deleteButton.addEventListener("click", async () => {
      await deleteCategory(category.id);
    });

    actionCell.appendChild(editButton);
    actionCell.appendChild(deleteButton);

    row.appendChild(idCell);
    row.appendChild(nameCell);
    row.appendChild(actionCell);

    tableBody.appendChild(row);
  });
}

async function handleSubmit(event) {
  event.preventDefault();

  const nameValue = nameInput ? nameInput.value.trim() : "";
  const idValue = categoryIdInput ? categoryIdInput.value : "";

  if (!nameValue) {
    showMessage("Ангиллын нэрийг оруулна уу", "error");
    return;
  }

  const categoryData = { name: nameValue };

  try {
    let response;

    if (!idValue) {
      response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(categoryData),
      });
    } else {
      response = await fetch(`${API_URL}/${idValue}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(categoryData),
      });
    }

    if (response.ok) {
      const msg = !idValue ? "Амжилттай үүсгэгдлээ" : "Амжилттай шинэчлэгдлээ";
      showMessage(msg, "success");
      resetForm();
      await loadCategories();
    } else {
      showMessage("Ажиллагаа амжилтгүй боллоо!", "error");
    }
  } catch (error) {
    console.error("Error submitting form:", error);
    showMessage("Илгээхэд алдаа гарлаа", "error");
  }
}

async function deleteCategory(id) {
  const confirmed = confirm("Устгахдаа итгэлтэй байна уу?");
  if (!confirmed) return;

  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
    });

    if (response.ok) {
      showMessage("Амжилттай устгагдлаа", "success");
      await loadCategories();
    } else {
      showMessage("Устгахад алдаа гарлаа", "error");
    }
  } catch (error) {
    console.error("Error deleting category:", error);
    showMessage("Сервертэй холбогдоход алдаа гарлаа", "error");
  }
}

function resetForm() {
  if (categoryIdInput) categoryIdInput.value = "";
  if (nameInput) nameInput.value = "";
  if (submitBtn) submitBtn.textContent = "Хадгалах";
}

function showMessage(msg, type = "success") {
  if (messageElement) {
    messageElement.textContent = msg;
    messageElement.style.color = type === "error" ? "red" : "green";

    setTimeout(() => {
      messageElement.textContent = "";
    }, 3000);
  }
}

if (form) {
  form.addEventListener("submit", handleSubmit);
}

loadCategories();