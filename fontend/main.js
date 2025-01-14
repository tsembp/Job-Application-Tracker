const baseURL = 'http://localhost:8080/api/jobapplications';
const form = document.getElementById('jobForm');
const applicationsList = document.getElementById('applicationsList');

// Add application
form.onsubmit = async(event) => {
    event.preventDefault();
    const company = document.getElementById('company').value;
    const position = document.getElementById('position').value;
    const location = document.getElementById('location').value;
    const applicationDate = document.getElementById('applicationDate').value;
    const status = document.getElementById('status').value;
    const jobType = document.getElementById('jobType').value;
    const notes = document.getElementById('notes').value;

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

// Get applications
async function fetchApplications() {
    const response = await fetch(baseURL);
    const data = await response.json(); // get applications in json format
    populateLocationDropdown(data);
    displayApplications(data);
}

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

// Filter functions
async function filterApplications() {
    const status = document.getElementById('statusFilter').value;
    const jobType = document.getElementById('jobTypeFilter').value;
    const location = document.getElementById('locationFilter').value;

    let url = `${baseURL}/filter`;
    const params = [];

    // add filters if applied
    if (status) params.push(`status=${encodeURIComponent(status)}`);
    if (jobType) params.push(`jobType=${encodeURIComponent(jobType)}`);
    if (location) params.push(`location=${encodeURIComponent(location)}`);

    if(params.length === 0){
        fetchApplications();
        return;
    }

    url += params.join('&'); // construct filter query URL
    
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to fetch');
        const data = await response.json();
        displayApplications(data);
    } catch (error) {
        alert("No matching applications found.");
        displayApplications([]); 
    }
}

async function searchApplications() {
    const keyword = document.getElementById('searchBar').value;
    const response = await fetch(`${baseURL}/search?keyword=${encodeURIComponent(keyword)}`);
    const data = await response.json();
    displayApplications(data);
}

function displayApplications(data) {
    const applicationsList = document.getElementById('applicationsList');
    applicationsList.innerHTML = '';

    if (data.length === 0) {
        applicationsList.innerHTML = `<tr><td colspan="7">No applications found.</td></tr>`;
        return;
    }

    data.forEach(app => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td contenteditable="true" onblur="updateApplication('${app.id}', 'company', this.innerText)">${app.company}</td>
            <td contenteditable="true" onblur="updateApplication('${app.id}', 'position', this.innerText)">${app.position}</td>
            
            <td>
                <select onchange="updateApplication('${app.id}', 'status', this.value)">
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
        `;
        applicationsList.appendChild(row);
    });
}

async function updateApplication(id, field, newValue){
    const response = await fetch(`${baseURL}/${id}`);
    if(!response.ok){
        alert("Error fetching the application data for update.");
        return;
    }

    const appData = await response.json();
    appData[field] = newValue;

    const updateResponse = await fetch(`${baseURL}/${id}`,{
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(appData)
    });
    
    if (updateResponse.ok) {
        alert("Application updated successfully!");
        fetchApplications();
    } else {
        alert("Failed to update the application.");
    }

}

fetchApplications();