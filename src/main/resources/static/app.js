document.addEventListener('DOMContentLoaded', () => {

    /* --- NAVIGATION --- */
    const navLinks = document.querySelectorAll('.nav-links li');
    const views = document.querySelectorAll('.view');

    // Simple Tab Switching Logic
    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            // Remove active classes
            navLinks.forEach(l => l.classList.remove('active'));
            views.forEach(v => v.classList.remove('active-view'));

            // Add active class
            link.classList.add('active');
            const tabId = link.getAttribute('data-tab');
            document.getElementById(`${tabId}-view`).classList.add('active-view');

            // Refresh Data specific to view
            if (tabId === 'books') loadBooks();
            if (tabId === 'members') loadMembers();
        });
    });


    /* --- BOOK MODAL HANDLING --- */
    const bookStage = document.getElementById('book-modal-overlay');
    const bookObject = document.querySelector('.book-object');
    // Open
    document.getElementById('add-book-btn').addEventListener('click', () => {
        bookStage.classList.remove('hidden');
        setTimeout(() => {
            bookObject.classList.add('is-open');
        }, 100);
    });
    // Close
    document.getElementById('cancel-book-btn').addEventListener('click', () => {
        bookObject.classList.remove('is-open');
        setTimeout(() => {
            bookStage.classList.add('hidden');
        }, 800); // Wait for close animation
    });

    /* --- MEMBER MODAL HANDLING --- */
    const memberModal = document.getElementById('member-modal-overlay');
    // Open
    document.getElementById('add-member-btn').addEventListener('click', () => {
        memberModal.classList.remove('hidden');
    });
    // Close
    document.getElementById('cancel-member-btn').addEventListener('click', () => {
        memberModal.classList.add('hidden');
    });


    /* --- FORM SUBMISSIONS --- */

    // 1. ADD BOOK
    document.getElementById('add-book-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const bookData = {
            ISBN: document.getElementById('isbn').value, // Matches Java entity field 'ISBN'
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            genre: document.getElementById('genre').value
        };

        try {
            await apiPost('/api/library/book', bookData);
            alert("Book Inscribed Successfully.");
            // Reset & Close
            document.getElementById('add-book-form').reset();
            document.getElementById('cancel-book-btn').click();
            loadBooks();
        } catch (err) {
            console.error(err);
            alert("Failed to add book.");
        }
    });

    // 2. ADD MEMBER
    document.getElementById('add-member-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const memberData = {
            memberId: document.getElementById('member-id').value,
            name: document.getElementById('member-name').value,
            email: document.getElementById('member-email').value,
            phone: document.getElementById('member-phone').value
        };

        try {
            await apiPost('/api/library/member', memberData);
            // Close
            memberModal.classList.add('hidden');
            document.getElementById('add-member-form').reset();
            loadMembers();
        } catch (err) {
            console.error(err);
            alert("Failed to add member.");
        }
    });

    // 3. CIRCULATION (Issue/Return)
    const circForm = document.getElementById('circulation-form');
    let circAction = 'issue'; // Default

    // Toggle Action
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            circAction = btn.getAttribute('data-action');

            // Adjust button text
            circForm.querySelector('button[type="submit"]').textContent =
                circAction === 'issue' ? 'Confirm Issue' : 'Confirm Return';
        });
    });

    circForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const memberId = document.getElementById('circ-member-id').value;
        const isbn = document.getElementById('circ-isbn').value;
        const msgBox = document.getElementById('circ-message');

        msgBox.textContent = "Processing...";
        msgBox.className = "status-message";

        try {
            // Endpoints: /api/library/issue or /api/library/return
            // Both take query params: ?isbn=...&member_id=...
            const url = `/api/library/${circAction}?isbn=${encodeURIComponent(isbn)}&member_id=${encodeURIComponent(memberId)}`;

            const response = await fetch(url, { method: 'POST' });

            if (response.ok) {
                msgBox.textContent = `Success: Book ${circAction}d.`;
                msgBox.classList.add('success');
                msgBox.style.color = 'green';
            } else {
                throw new Error('Action failed');
            }
        } catch (err) {
            msgBox.textContent = "Error: Could not complete transaction.";
            msgBox.style.color = 'red';
        }
    });


    /* --- DATA LOADERS --- */

    async function loadBooks() {
        const grid = document.getElementById('book-grid');
        grid.innerHTML = '<p>Loading library...</p>';
        try {
            const books = await apiGet('/api/library/books');
            grid.innerHTML = '';

            if (books.length === 0) {
                grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: #999;">Library is empty.</div>';
                return;
            }

            books.forEach(book => {
                const card = document.createElement('div');
                card.className = 'book-card';
                // Dynamic gradient based on title length or random
                card.innerHTML = `
                    <div class="card-cover">
                        <span class="card-status ${book.available ? 'status-available' : 'status-out'}">
                            ${book.available ? 'Available' : 'Out'}
                        </span>
                        <span>${book.title.charAt(0)}</span>
                    </div>
                    <div class="card-details">
                        <h3 class="card-title" title="${book.title}">${book.title}</h3>
                        <p class="card-subtitle">${book.author}</p>
                        <p class="card-subtitle" style="font-size: 0.75rem; margin-top: 4px; color: #bbb;">${book.isbn || book.ISBN}</p>
                    </div>
                `;
                grid.appendChild(card);
            });
        } catch (err) {
            grid.innerHTML = '<p>Failed to load books.</p>';
        }
    }

    async function loadMembers() {
        const list = document.getElementById('member-list');
        list.innerHTML = '<p>Loading members...</p>';
        try {
            const members = await apiGet('/api/library/members');
            list.innerHTML = '';

            if (members.length === 0) {
                list.innerHTML = '<div style="text-align: center; color: #999; padding: 2rem;">No members found.</div>';
                return;
            }

            members.forEach(m => {
                const row = document.createElement('div');
                row.className = 'member-row';
                row.innerHTML = `
                    <div style="display: flex; align-items: center;">
                        <div class="member-avatar">
                            ${m.name.charAt(0)}
                        </div>
                        <div class="member-info">
                            <div class="member-name">${m.name}</div>
                            <div class="member-sub">${m.email}</div>
                        </div>
                    </div>
                    <div style="color: #888; font-size: 0.9rem;">
                        ID: <strong>${m.memberId}</strong>
                    </div>
                `;
                list.appendChild(row);
            });
        } catch (err) {
            list.innerHTML = '<p>Failed to load members.</p>';
        }
    }


    /* --- API UTILS --- */
    async function apiGet(url) {
        const res = await fetch(url);
        if (!res.ok) throw new Error(`GET ${url} failed`);
        return await res.json();
    }

    async function apiPost(url, data) {
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!res.ok) throw new Error(`POST ${url} failed`);
        return res; // Can return json if needed, but often void
    }

    // Initial Load
    loadBooks();

});
