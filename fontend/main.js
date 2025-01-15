const baseURL = 'http://localhost:8080/api/jobapplications';
const form = document.getElementById('jobForm');
const applicationsList = document.getElementById('applicationsList');

let currentPage = 1;
let entriesPerPage = 5;
let allApplications = [];
let filteredApplications = [];

let jobTypeChartInstance = null;
let statusChartInstance = null;

// Add application
form.onsubmit = async(event) => {
    event.preventDefault();

    // Get user's input and clear fields
    const company = document.getElementById('company').value;
    document.getElementById('company').value = '';

    const position = document.getElementById('position').value;
    document.getElementById('position').value = '';

    const location = document.getElementById('location').value;
    document.getElementById('location').value = '';

    const applicationDate = document.getElementById('applicationDate').value;
    document.getElementById('applicationDate').value = '';

    const status = document.getElementById('status').value;
    status.selectedIndex = 0;

    const jobType = document.getElementById('jobType').value;
    jobType.selectedIndex = 0;

    const notes = document.getElementById('notes').value;
    document.getElementById('notes').value = '';

    // Example JSON:
    // {
    //     "company": "Amazon",
    //     "position": "AI Software Engineer",
    //     "status": "REJECTED",
    //     "applicationDate": "02-01-2025",
    //     "notes": "Looking forward to receiving a reply.",
    //     "jobType" : "HYBRID",
    //     "location" : "Amsterdam, Netherlands"
    // }

    // New job application with its attributes
    const jobApplication = {
        company: company,
        position: position,
        status: status,
        applicationDate: applicationDate,
        notes: notes,
        jobType: jobType,
        location: location
    }

    // POST request to backend
    const response = await fetch(baseURL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jobApplication)
    });

    if(response.ok){
        console.log('Application added successfully');
        fetchApplications();
    } else{
        console.log('Error adding application');
    }
}

// Update existing application
async function updateApplication(id, field, newValue) {
    try {
        const response = await fetch(`${baseURL}/${id}`); // fetch application
        if (!response.ok) {
            alert("Error fetching the application data for update.");
            return;
        }

        const appData = await response.json();

        if (appData[field] == newValue) { // skip if unchanged
            fetchApplications(); // refresh data
            return;
        }

        appData[field] = newValue; // update field

        const updateResponse = await fetch(`${baseURL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(appData)
        });

        if (updateResponse.ok) {
            alert("Application updated successfully!");
            fetchApplications(); // refresh data
        } else {
            alert("Failed to update the application.");
        }
    } catch (error) {
        console.error("Error during update:", error);
        alert("An error occurred while updating the application.");
    }
}

// Delete application by id
async function deleteApplication(id) {
    if (confirm("Are you sure you want to delete this application?")) { // confirmation window
        const response = await fetch(`${baseURL}/${id}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            alert("Application deleted successfully!");
            fetchApplications(); // refresh the table after deletion
        } else {
            alert("Failed to delete the application.");
        }
    }
}

// Get applications
async function fetchApplications() {
    try{
        const response = await fetch(baseURL);
        if(!response.ok) throw new Error('Failed to fetch applications');
        allApplications = await response.json(); // get applications in json format
        generateCharts(allApplications); // charts generation
        updateHeroSection(allApplications);
        populateLocationDropdown(allApplications); // get values for location dropdown
        displayApplications(allApplications); // display table
    } catch(error){
        console.error('Error fetching applications:', error);
    }
}

// Search by keyword & filters if applied
async function filterApplications() {
    const keyword = document.getElementById('searchBar').value.trim();
    const filterState = {
        status: document.getElementById('statusFilter').value,
        jobType: document.getElementById('jobTypeFilter').value,
        location: document.getElementById('locationFilter').value
    }

    if(!filterState){
        filteredApplications = [];
    }

    let url = `${baseURL}/filter`;
    const params = [];

    // Combine keyword with existing filters
    if (keyword) params.push(`keyword=${encodeURIComponent(keyword)}`);
    if (filterState.status) params.push(`status=${encodeURIComponent(filterState.status)}`);
    if (filterState.jobType) params.push(`jobType=${encodeURIComponent(filterState.jobType)}`);
    if (filterState.location) params.push(`location=${encodeURIComponent(filterState.location)}`);

    if (params.length > 0) {
        url += `?${params.join('&')}`;
    }

    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to fetch');
        filteredApplications = await response.json();
        currentPage = 1;
        displayApplications(filteredApplications);
    } catch (error) {
        alert("No matching applications found.");
        displayApplications([]); 
    }
}

// Show applications
function displayApplications(data) {
    const applicationsList = document.getElementById('applicationsList');
    applicationsList.innerHTML = '';

    const startIndex = (currentPage - 1) * entriesPerPage;
    const endIndex = startIndex + entriesPerPage;
    const paginatedData = data.slice(startIndex, endIndex);

    if (data.length === 0) {
        applicationsList.innerHTML = `<tr><td colspan="7">No applications found.</td></tr>`;
        return;
    }

    paginatedData.forEach(app => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td contenteditable="true" onblur="updateApplication('${app.id}', 'company', this.innerText)">${app.company}</td>
            <td contenteditable="true" onblur="updateApplication('${app.id}', 'position', this.innerText)">${app.position}</td>
            
            <td>
                <select class = "status-select" onchange="updateApplication('${app.id}', 'status', this.value)">
                    <option value="APPLIED" ${app.status === 'APPLIED' ? 'selected' : ''}>Applied</option>
                    <option value="REJECTED" ${app.status === 'REJECTED' ? 'selected' : ''}>Rejected</option>
                    <option value="INTERVIEW" ${app.status === 'INTERVIEW' ? 'selected' : ''}>Interview</option>
                    <option value="OFFERED" ${app.status === 'OFFERED' ? 'selected' : ''}>Offered</option>
                </select>
            </td>

            <td>
                <select onchange="updateApplication('${app.id}', 'jobType', this.value)">
                    <option value="ONSITE" ${app.jobType === 'ONSITE' ? 'selected' : ''}>Onsite</option>
                    <option value="REMOTE" ${app.jobType === 'REMOTE' ? 'selected' : ''}>Remote</option>
                    <option value="HYBRID" ${app.jobType === 'HYBRID' ? 'selected' : ''}>Hybrid</option>
                </select>
            </td>

            <td contenteditable="true" onblur="updateApplication('${app.id}', 'location', this.innerText)">${app.location}</td>

            <td>
                <input type="date" value="${app.applicationDate}" onchange="updateApplication('${app.id}', 'applicationDate', this.value)">
            </td>

            <td contenteditable="true" onblur="updateApplication('${app.id}', 'notes', this.innerText)">${app.notes}</td>

            <td>
                <button onclick="deleteApplication('${app.id}')" style="border: none; background: none; cursor: pointer; display: flex; justify-content: center; align-items: center; width: 100%;">
                    🗑️
                </button>
            </td>
        `;
        applicationsList.appendChild(row);
    });

    updatePaginationControls(data.length);
}

// Function to update pages for table
function updatePaginationControls(totalEntries) {
    const paginationControls = document.getElementById('paginationControls');
    paginationControls.innerHTML = '';

    const totalPages = Math.ceil(totalEntries / entriesPerPage);

    // Create Previous Button
    const prevButton = document.createElement('button');
    prevButton.textContent = 'Previous';
    prevButton.onclick = () => changePage(-1);
    prevButton.disabled = currentPage === 1;
    paginationControls.appendChild(prevButton);

    // Create Page Number Info
    const pageInfo = document.createElement('span');
    pageInfo.textContent = ` Page ${currentPage} of ${totalPages} `;
    paginationControls.appendChild(pageInfo);

    // Create Next Button
    const nextButton = document.createElement('button');
    nextButton.textContent = 'Next';
    nextButton.onclick = () => changePage(1);
    nextButton.disabled = currentPage === totalPages;
    paginationControls.appendChild(nextButton);
}

// Function to Change Page
function changePage(step) {
    currentPage += step;
    const dataToDisplay = filteredApplications.length > 0 ? filteredApplications : allApplications;
    displayApplications(dataToDisplay);
}

// Get location values for dropdown
function populateLocationDropdown(data) {
    const locationFilter = document.getElementById('locationFilter');
    locationFilter.innerHTML = '<option value="">Select Location</option>'; // Clear existing options

    // Extract unique locations using Set
    const uniqueLocations = [...new Set(data.map(app => app.location))];

    // Populate dropdown with unique locations
    uniqueLocations.forEach(location => {
        const option = document.createElement('option');
        option.value = location;
        option.textContent = location;
        locationFilter.appendChild(option);
    });
}

// Function to display key job application stats
function updateHeroSection(data) {
    // Total Applications
    document.getElementById('totalApplications').textContent = data.length;

    // Total Interviews (Count based on status)
    const totalInterviews = data.filter(app => app.status === 'INTERVIEW').length;
    document.getElementById('totalInterviews').textContent = totalInterviews;

    // Total Offers (Count based on status)
    const totalOffers = data.filter(app => app.status === 'OFFERED').length;
    document.getElementById('totalOffers').textContent = totalOffers;
}

// Function to generate charts for jobType and status
function generateCharts(data) {
    console.log('Updating graphs.')
    const jobTypeCounts = {};
    const statusCounts = {};

    // Count jobType and status data
    data.forEach(app => {
        jobTypeCounts[app.jobType] = (jobTypeCounts[app.jobType] || 0) + 1;
        statusCounts[app.status] = (statusCounts[app.status] || 0) + 1;
    });

    // Destroy existing charts before re-creating them
    if (jobTypeChartInstance) {
        jobTypeChartInstance.destroy();
    }
    if (statusChartInstance) {
        statusChartInstance.destroy();
    }

    // Job Type Chart
    const jobTypeCtx = document.getElementById('jobTypeChart').getContext('2d');
    jobTypeChartInstance = new Chart(jobTypeCtx, {
        type: 'pie',
        data: {
            labels: Object.keys(jobTypeCounts),
            datasets: [{
                data: Object.values(jobTypeCounts),
                backgroundColor: ['#4caf50', '#2196f3', '#ff9800'],
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            aspectRatio: 1,
            layout: {
                padding: {
                    top: 20,
                    bottom: 20,
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        padding: 30,
                    }
                }
            }
        }
    });

    // Status Chart
    const statusCtx = document.getElementById('statusChart').getContext('2d');
    statusChartInstance = new Chart(statusCtx, {
        type: 'bar',
        data: {
            labels: Object.keys(statusCounts),
            datasets: [{
                label: 'Number of Applications',
                data: Object.values(statusCounts),
                backgroundColor: '#fcba03',
            }]
        }
    });
}

// Export CSV
function exportCSV() {
    window.location.href = `${baseURL}/export`;
}

fetchApplications();