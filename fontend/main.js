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
            <td>${app.company}</td>
            <td>${app.position}</td>
            <td>${app.status}</td>
            <td>${app.jobType}</td>
            <td>${app.location}</td>
            <td>${app.applicationDate}</td>
            <td>${app.notes}</td>
        `;
        applicationsList.appendChild(row);
    });
}

fetchApplications();