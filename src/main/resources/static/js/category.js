"use strict";

const API_URL = "/api/categories";

const form = document.querySelector("#category-form");
const categoryIdInput = document.querySelector("#category-id");
const nameInput = document.querySelector("#category-name");
const descriptionInput = document.querySelector("#category-description");
const tableBody = document.querySelector("#category-table-body");
const submitButton = document.querySelector("#submit-button");
const cancelButton = document.querySelector("#cancel-button");
const refreshButton = document.querySelector("#refresh-button");
const formTitle = document.querySelector("#form-title");
const messageElement = document.querySelector("#message");

async function loadCategories() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error("Categories could not be loaded.");
    }
    const categories = await response.json();
    renderCategories(categories);
  } catch (error) {
    console.error(error);
    showMessage("Categories could not be loaded.");
  }
}

function renderCategories(categories) {
  if (!tableBody) return;
  tableBody.innerHTML = "";

  if (categories.length === 0) {
    const row = document.createElement("tr");
    const cell = document.createElement("td");
    cell.colSpan = 3;
    cell.style.textAlign = "center";
    cell.textContent = "Categories not found";

    row.appendChild(cell);
    tableBody.appendChild(row);
    return;
  }

  for (let category of categories) {
    const row = document.createElement("tr");

    const idCell = document.createElement("td");
    idCell.textContent = category.id;

    const nameCell = document.createElement("td");
    nameCell.textContent = category.name;

    const actionCell = document.createElement("td");

    const editButton = document.createElement("button");
    editButton.type = "button";
    editButton.textContent = "Edit";
    editButton.classList.add("button", "secondary");
    editButton.style.marginRight = "5px";
    editButton.addEventListener("click", () => {
      startEdit(category);
    });

    const deleteButton = document.createElement("button");
    deleteButton.type = "button";
    deleteButton.textContent = "Delete";
    deleteButton.classList.add("button", "danger");
    deleteButton.addEventListener("click", () => {
      deleteCategory(category.id);
    });

    actionCell.appendChild(editButton);
    actionCell.appendChild(deleteButton);

    row.appendChild(idCell);
    row.appendChild(nameCell);
    row.appendChild(actionCell);

    tableBody.appendChild(row);
  }
}

async function handleSubmit(e) {
  e.preventDefault();

  const id = categoryIdInput ? categoryIdInput.value : "";
  const nameValue = nameInput ? nameInput.value.trim() : "";
  const descriptionValue = descriptionInput ? descriptionInput.value.trim() : "";

  if (!nameValue) {
    showMessage("Category name is required.");
    return;
  }

  const categoryData = {
    name: nameValue,
    description: descriptionValue,
  };

  const isEditing = id !== "";
  const url = isEditing ? `${API_URL}/${id}` : API_URL;
  const method = isEditing ? "PUT" : "POST";

  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(categoryData),
    });

    if (!response.ok) {
      throw new Error("Request failed.");
    }

    const successMsg = isEditing
      ? "Category updated successfully."
      : "Category created successfully.";

    showMessage(successMsg);
    resetForm();
    await loadCategories();
  } catch (error) {
    console.error(error);
    showMessage("Request Failed!");
  }
}

async function deleteCategory(id) {
  const confirmed = confirm("Are you sure you want to delete this category?");
  if (!confirmed) return;

  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      throw new Error("Delete failed.");
    }

    showMessage("Category deleted successfully.");
    resetForm();
    await loadCategories();
  } catch (error) {
    console.error(error);
    showMessage("Category could not be deleted.");
  }
}

function startEdit(category) {
  if (categoryIdInput) categoryIdInput.value = category.id;
  if (nameInput) nameInput.value = category.name;
  if (descriptionInput) descriptionInput.value = category.description ?? "";
  
  if (formTitle) formTitle.textContent = "Edit Category";
  if (submitButton) submitButton.textContent = "Update Category";
  if (cancelButton) cancelButton.hidden = false;
}

function resetForm() {
  if (form) form.reset();
  if (categoryIdInput) categoryIdInput.value = "";
  if (nameInput) nameInput.value = "";
  if (descriptionInput) descriptionInput.value = "";
  
  if (formTitle) formTitle.textContent = "Add Category";
  if (submitButton) submitButton.textContent = "Add Category";
  if (cancelButton) cancelButton.hidden = true;
}

function showMessage(text) {
  if (messageElement) {
    messageElement.textContent = text;
    messageElement.hidden = false;
    setTimeout(() => {
      messageElement.hidden = true;
    }, 3000);
  }
}

// Event Listeners
if (form) {
  form.addEventListener("submit", handleSubmit);
}

if (cancelButton) {
  cancelButton.addEventListener("click", resetForm);
  cancelButton.hidden = true; // Эхэнд нь нууна
}

if (refreshButton) {
  refreshButton.addEventListener("click", loadCategories);
}

loadCategories();