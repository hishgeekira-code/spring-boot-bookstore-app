const form = document.querySelector('category-form');
const categoryIdInput = document.querySelector('category-id');
const nameInput = document.querySelector('category-name');
const tableBody = document.querySelector('category-table-body');
const API_URL = "http://localhost:8080/api/categories";

console.log("Category JavaScript loaded");


// Category-nuudiig table-d haruulah
async function loadCategories() {
	
	const response = await fetch(
		API_URL	
	);
	
	const data = await response.json();
	
	const categories = data;
	console.log(categories);
}

loadCategories();


// Category list deer loop hiih
function renderCategories(categories) {
    const tableBody = document.getElementById("category-table-body");
    tableBody.innerHTML = "";
    
    for (const category of categories) {

        const row = document.createElement("tr");
        
        const idCell = document.createElement("td");
        idCell.textContent = category.id;
        row.appendChild(idCell);

        const nameCell = document.createElement("td");
        nameCell.textContent = category.name;
        row.appendChild(nameCell);
        
        tableBody.appendChild(row);
    }
}
