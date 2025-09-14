// JavaScript functionality for C# Cheatsheet Application

// DOM Content Loaded Event
document.addEventListener('DOMContentLoaded', function() {
    console.log('C# Cheatsheet Application loaded successfully');
    
    // Initialize all components
    initializeCopyButtons();
    initializeNavigation();
    initializeCodeHighlighting();
    initializeSmoothScrolling();
    initializeTooltips();
});

// Copy to clipboard functionality
function initializeCopyButtons() {
    // Add copy buttons to all code blocks
    const codeBlocks = document.querySelectorAll('.code-block');
    
    codeBlocks.forEach(block => {
        // Create copy button if it doesn't exist
        if (!block.querySelector('.copy-btn')) {
            const copyBtn = document.createElement('button');
            copyBtn.className = 'copy-btn';
            copyBtn.textContent = 'Copy';
            copyBtn.onclick = () => copyCodeToClipboard(block, copyBtn);
            block.appendChild(copyBtn);
        }
    });
}

// Copy code to clipboard function
function copyCodeToClipboard(codeBlock, button) {
    const code = codeBlock.querySelector('pre')?.textContent || codeBlock.textContent;
    
    navigator.clipboard.writeText(code).then(() => {
        // Success feedback
        button.textContent = 'Copied!';
        button.classList.add('copied');
        
        setTimeout(() => {
            button.textContent = 'Copy';
            button.classList.remove('copied');
        }, 2000);
    }).catch(err => {
        console.error('Failed to copy: ', err);
        // Fallback for older browsers
        const textArea = document.createElement('textarea');
        textArea.value = code;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
        
        button.textContent = 'Copied!';
        setTimeout(() => {
            button.textContent = 'Copy';
        }, 2000);
    });
}

// Navigation functionality
function initializeNavigation() {
    const navItems = document.querySelectorAll('.nav-item');
    
    navItems.forEach(item => {
        item.addEventListener('click', function(e) {
            // Handle internal anchor links
            const href = this.getAttribute('href');
            if (href && href.startsWith('#')) {
                e.preventDefault();
                const target = document.querySelector(href);
                if (target) {
                    target.scrollIntoView({ 
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            }
            
            // Update active navigation item
            navItems.forEach(nav => nav.classList.remove('active'));
            this.classList.add('active');
        });
    });
    
    // Update active nav based on scroll position
    window.addEventListener('scroll', updateActiveNavOnScroll);
}

// Update active navigation based on scroll position
function updateActiveNavOnScroll() {
    const sections = document.querySelectorAll('[id]');
    const navItems = document.querySelectorAll('.nav-item[href^="#"]');
    
    let current = '';
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        if (window.pageYOffset >= sectionTop - 100) {
            current = section.getAttribute('id');
        }
    });
    
    navItems.forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('href') === '#' + current) {
            item.classList.add('active');
        }
    });
}

// Code highlighting (basic syntax highlighting)
function initializeCodeHighlighting() {
    const codeBlocks = document.querySelectorAll('.code-block pre');
    
    codeBlocks.forEach(block => {
        let code = block.innerHTML;
        
        // Basic C# syntax highlighting
        code = code.replace(/\b(public|private|protected|internal|static|readonly|const|virtual|override|abstract|sealed|class|interface|struct|enum|namespace|using|if|else|for|foreach|while|do|switch|case|default|break|continue|return|try|catch|finally|throw|new|this|base|null|true|false|var|int|string|bool|double|float|decimal|char|byte|short|long|object|void)\b/g, '<span class="keyword">$1</span>');
        
        // String highlighting
        code = code.replace(/"([^"\\]|\\.)*"/g, '<span class="string">$&</span>');
        
        // Comment highlighting
        code = code.replace(/\/\/.*$/gm, '<span class="comment">$&</span>');
        code = code.replace(/\/\*[\s\S]*?\*\//g, '<span class="comment">$&</span>');
        
        // Number highlighting
        code = code.replace(/\b\d+(\.\d+)?\b/g, '<span class="number">$&</span>');
        
        block.innerHTML = code;
    });
}

// Smooth scrolling for all internal links
function initializeSmoothScrolling() {
    const links = document.querySelectorAll('a[href^="#"]');
    
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            const targetId = this.getAttribute('href');
            const target = document.querySelector(targetId);
            
            if (target) {
                e.preventDefault();
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Initialize tooltips
function initializeTooltips() {
    const tooltipElements = document.querySelectorAll('[data-tooltip]');
    
    tooltipElements.forEach(element => {
        element.addEventListener('mouseenter', showTooltip);
        element.addEventListener('mouseleave', hideTooltip);
    });
}

// Show tooltip
function showTooltip(e) {
    const tooltip = document.createElement('div');
    tooltip.className = 'tooltip';
    tooltip.textContent = e.target.getAttribute('data-tooltip');
    
    document.body.appendChild(tooltip);
    
    const rect = e.target.getBoundingClientRect();
    tooltip.style.left = rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2) + 'px';
    tooltip.style.top = rect.top - tooltip.offsetHeight - 8 + 'px';
    
    e.target.tooltipElement = tooltip;
}

// Hide tooltip
function hideTooltip(e) {
    if (e.target.tooltipElement) {
        document.body.removeChild(e.target.tooltipElement);
        e.target.tooltipElement = null;
    }
}

// Search functionality
function initializeSearch() {
    const searchInput = document.getElementById('search-input');
    const searchResults = document.getElementById('search-results');
    
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const query = this.value.toLowerCase().trim();
            
            if (query.length > 2) {
                performSearch(query);
            } else {
                clearSearchResults();
            }
        });
    }
}

// Perform search
function performSearch(query) {
    const sections = document.querySelectorAll('.card, .section');
    const results = [];
    
    sections.forEach(section => {
        const text = section.textContent.toLowerCase();
        if (text.includes(query)) {
            const title = section.querySelector('.card-title, h2, h3')?.textContent || 'Untitled';
            results.push({
                title: title,
                element: section
            });
        }
    });
    
    displaySearchResults(results);
}

// Display search results
function displaySearchResults(results) {
    const searchResults = document.getElementById('search-results');
    if (!searchResults) return;
    
    searchResults.innerHTML = '';
    
    if (results.length === 0) {
        searchResults.innerHTML = '<div class="no-results">No results found</div>';
        return;
    }
    
    results.forEach(result => {
        const resultItem = document.createElement('div');
        resultItem.className = 'search-result-item';
        resultItem.textContent = result.title;
        resultItem.onclick = () => {
            result.element.scrollIntoView({ behavior: 'smooth' });
            clearSearchResults();
        };
        searchResults.appendChild(resultItem);
    });
}

// Clear search results
function clearSearchResults() {
    const searchResults = document.getElementById('search-results');
    if (searchResults) {
        searchResults.innerHTML = '';
    }
}

// Database connection status checker
function checkDatabaseStatus() {
    fetch('MyServlet')
        .then(response => response.text())
        .then(html => {
            console.log('Database status checked');
            // Could parse the response to show status in UI
        })
        .catch(error => {
            console.error('Error checking database status:', error);
        });
}

// Theme switcher (if implemented)
function initializeThemeSwitcher() {
    const themeToggle = document.getElementById('theme-toggle');
    
    if (themeToggle) {
        themeToggle.addEventListener('click', function() {
            document.body.classList.toggle('dark-theme');
            
            // Save theme preference
            const isDark = document.body.classList.contains('dark-theme');
            localStorage.setItem('theme', isDark ? 'dark' : 'light');
        });
        
        // Load saved theme
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme === 'dark') {
            document.body.classList.add('dark-theme');
        }
    }
}

// Utility functions
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.classList.add('fade-out');
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

function debounce(func, delay) {
    let timeoutId;
    return function (...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func.apply(this, args), delay);
    };
}

// Export functions for global access
window.CheatsheetApp = {
    copyCodeToClipboard,
    showNotification,
    checkDatabaseStatus,
    performSearch
};