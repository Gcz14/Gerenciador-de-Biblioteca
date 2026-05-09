let token = null;
let userId = null;
let userEmail = null;
let userNome = null;
let livroEditandoId = null; // ID do livro que está sendo editado

const API_URL = 'http://localhost:9999/api';

// Função para mostrar toast (notificação visual)
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

// Função para mostrar toast com botão de confirmação (CORRIGIDA)
function showConfirmToast(message, onConfirm) {
    const toast = document.createElement('div');
    toast.className = `toast info`;
    toast.style.padding = '15px 20px';
    toast.style.minWidth = '250px';
    toast.style.cursor = 'default';
    toast.innerHTML = `
        <div style="margin-bottom: 10px;">${message}</div>
        <div style="display: flex; gap: 10px; justify-content: flex-end;">
            <button class="confirm-yes" style="background: #48bb78; padding: 5px 15px; font-size: 12px; border: none; border-radius: 5px; cursor: pointer;">Sim</button>
            <button class="confirm-no" style="background: #e53e3e; padding: 5px 15px; font-size: 12px; border: none; border-radius: 5px; cursor: pointer;">Não</button>
        </div>
    `;
    document.body.appendChild(toast);
    
    // Adicionar eventos diretamente nos botões
    const yesBtn = toast.querySelector('.confirm-yes');
    const noBtn = toast.querySelector('.confirm-no');
    
    yesBtn.onclick = () => {
        toast.remove();
        onConfirm();
    };
    
    noBtn.onclick = () => {
        toast.remove();
    };
    
    // Auto remover após 10 segundos
    setTimeout(() => {
        if (toast.parentNode) toast.remove();
    }, 10000);
}

// Função para buscar dados do usuário logado
async function fetchUserData() {
    try {
        const response = await fetch(`${API_URL}/users/${userId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (response.ok) {
            const userData = await response.json();
            userNome = userData.nome || userEmail.split('@')[0];
            updateUserMenu();
            return userData;
        } else {
            userNome = userEmail.split('@')[0];
            updateUserMenu();
            return null;
        }
    } catch (error) {
        console.error('Erro ao buscar dados do usuário:', error);
        userNome = userEmail.split('@')[0];
        updateUserMenu();
    }
}

// Atualizar o menu do usuário na tela
function updateUserMenu() {
    const userNameSpan = document.getElementById('userName');
    if (userNameSpan && userNome) {
        userNameSpan.textContent = `Olá, ${userNome}`;
    }
}

// Mostrar menu do usuário (quando logado)
function showUserMenu() {
    const userMenu = document.getElementById('userMenu');
    if (userMenu) {
        userMenu.classList.remove('hidden');
    }
}

// Esconder menu do usuário (quando deslogado)
function hideUserMenu() {
    const userMenu = document.getElementById('userMenu');
    if (userMenu) {
        userMenu.classList.add('hidden');
    }
}

// Fazer logout
function logout() {
    token = null;
    userId = null;
    userEmail = null;
    userNome = null;
    livroEditandoId = null;
    
    hideUserMenu();
    document.getElementById('loginCard').classList.remove('hidden');
    document.getElementById('livrosCard').classList.add('hidden');
    document.getElementById('loginEmail').value = '';
    document.getElementById('loginSenha').value = '';
    
    // Resetar botão de adicionar/atualizar
    resetarFormularioLivro();
    
    showToast('Logout realizado com sucesso!', 'info');
}

// Resetar formulário de livro para modo "Adicionar"
function resetarFormularioLivro() {
    livroEditandoId = null;
    const btnAdicionar = document.querySelector('#livrosCard button[onclick="criarLivro()"]');
    if (btnAdicionar) {
        btnAdicionar.textContent = 'Adicionar Livro';
        btnAdicionar.onclick = criarLivro;
        btnAdicionar.style.background = 'linear-gradient(135deg, #a7c7cb 0%, #7eadb3 100%)';
    }
    
    // Limpar campos
    document.getElementById('livroTitulo').value = '';
    document.getElementById('livroAutor').value = '';
    document.getElementById('livroIsbn').value = '';
    document.getElementById('livroAno').value = '';
    document.getElementById('livroCategoria').value = '';
    document.getElementById('livroDescricao').value = '';
    document.getElementById('livroStatus').value = 'DISPONIVEL';
    
    // Resetar validações visuais
    const isbnInput = document.getElementById('livroIsbn');
    const anoInput = document.getElementById('livroAno');
    const isbnReq = document.getElementById('isbnRequisito');
    const anoReq = document.getElementById('anoRequisito');
    
    if (isbnInput) isbnInput.classList.remove('invalid');
    if (anoInput) anoInput.classList.remove('invalid');
    if (isbnReq) isbnReq.style.color = '#ed8936';
    if (anoReq) anoReq.style.color = '#ed8936';
    if (isbnReq) isbnReq.classList.remove('valido');
    if (anoReq) anoReq.classList.remove('valido');
}

// Preencher formulário com dados do livro para edição
async function editarLivro(id) {
    try {
        const response = await fetch(`${API_URL}/livros/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (!response.ok) {
            showToast('❌ Erro ao buscar dados do livro', 'error');
            return;
        }
        
        const livro = await response.json();
        
        // Preencher o formulário com os dados do livro
        document.getElementById('livroTitulo').value = livro.titulo;
        document.getElementById('livroAutor').value = livro.autor;
        document.getElementById('livroIsbn').value = livro.isbn || '';
        document.getElementById('livroAno').value = livro.anoPublicacao || '';
        document.getElementById('livroCategoria').value = livro.categoria || '';
        document.getElementById('livroDescricao').value = livro.descricao || '';
        document.getElementById('livroStatus').value = livro.status;
        
        // Guardar o ID do livro que está sendo editado
        livroEditandoId = id;
        
        // Mudar o botão de "Adicionar Livro" para "Atualizar Livro"
        const btnAdicionar = document.querySelector('#livrosCard button[onclick="criarLivro()"]');
        if (btnAdicionar) {
            btnAdicionar.textContent = 'Atualizar Livro';
            btnAdicionar.onclick = atualizarLivro;
            btnAdicionar.style.background = 'linear-gradient(135deg, #48bb78 0%, #38a169 100%)';
        }
        
        // Rolar a tela até o formulário
        document.querySelector('#livrosCard .form-group').scrollIntoView({ behavior: 'smooth' });
        
        showToast('✏️ Editando livro. Altere os campos e clique em Atualizar.', 'info');
        
    } catch (error) {
        console.error('Erro:', error);
        showToast('❌ Erro ao carregar dados do livro', 'error');
    }
}

// Função para atualizar livro (usada quando está em modo edição)
async function atualizarLivro() {
    const titulo = document.getElementById('livroTitulo').value.trim();
    const autor = document.getElementById('livroAutor').value.trim();
    const isbn = document.getElementById('livroIsbn').value.trim();
    const ano = document.getElementById('livroAno').value.trim();
    const categoria = document.getElementById('livroCategoria').value.trim();
    const descricao = document.getElementById('livroDescricao').value.trim();
    const status = document.getElementById('livroStatus').value;

    // Validações
    if (!titulo) {
        showToast('⚠️ Título é obrigatório!', 'error');
        document.getElementById('livroTitulo').focus();
        return;
    }
    if (!autor) {
        showToast('⚠️ Autor é obrigatório!', 'error');
        document.getElementById('livroAutor').focus();
        return;
    }
    
    // Validação do ISBN (13 dígitos)
    const isbnNumeros = isbn.replace(/\D/g, '');
    if (isbn && isbnNumeros.length !== 13) {
        showToast('⚠️ ISBN deve ter exatamente 13 dígitos numéricos!', 'error');
        document.getElementById('livroIsbn').focus();
        return;
    }
    
    // Validação do Ano (4 dígitos)
    const anoRegex = /^\d{4}$/;
    if (ano && !anoRegex.test(ano)) {
        showToast('⚠️ Ano deve ter 4 dígitos (ex: 2024)!', 'error');
        document.getElementById('livroAno').focus();
        return;
    }

    const livro = {
        titulo: titulo,
        autor: autor,
        isbn: isbnNumeros || null,
        anoPublicacao: ano ? parseInt(ano) : null,
        categoria: categoria || null,
        descricao: descricao || null,
        status: status
    };

    try {
        const response = await fetch(`${API_URL}/livros/${livroEditandoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(livro)
        });

        if (response.ok) {
            showToast('✅ Livro atualizado com sucesso!', 'success');
            resetarFormularioLivro();
            carregarLivros();
        } else {
            const error = await response.json();
            if (error.fields) {
                let errorMsg = '';
                for (const [field, message] of Object.entries(error.fields)) {
                    errorMsg += `${field}: ${message}\n`;
                }
                showToast(`❌ ${errorMsg}`, 'error');
            } else {
                showToast(`❌ Erro: ${error.message || 'Erro ao atualizar livro'}`, 'error');
            }
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('❌ Erro ao conectar com o servidor: ' + error.message, 'error');
    }
}

// Toggle do dropdown
function toggleDropdown() {
    const dropdown = document.getElementById('userDropdown');
    const chevron = document.getElementById('chevron');
    
    if (dropdown.classList.contains('hidden')) {
        dropdown.classList.remove('hidden');
        chevron.classList.add('rotated');
    } else {
        dropdown.classList.add('hidden');
        chevron.classList.remove('rotated');
    }
}

// Fechar dropdown se clicar fora
function closeDropdownOnClickOutside(event) {
    const userMenu = document.getElementById('userMenu');
    const trigger = document.getElementById('userTrigger');
    
    if (userMenu && trigger && !userMenu.contains(event.target) && !trigger.contains(event.target)) {
        const dropdown = document.getElementById('userDropdown');
        const chevron = document.getElementById('chevron');
        if (dropdown && !dropdown.classList.contains('hidden')) {
            dropdown.classList.add('hidden');
            chevron.classList.remove('rotated');
        }
    }
}

// Validação de senha em tempo real
function validarSenha() {
    const senhaInput = document.getElementById('registerSenha');
    const senhaRequisito = document.getElementById('senhaRequisito');
    const senha = senhaInput.value;
    
    if (senha.length > 0 && senha.length < 6) {
        senhaInput.classList.add('invalid');
        senhaRequisito.style.color = '#ed8936';
        senhaRequisito.classList.remove('valido');
        return false;
    } else if (senha.length >= 6) {
        senhaInput.classList.remove('invalid');
        senhaRequisito.style.color = '#7eadb3';
        senhaRequisito.classList.add('valido');
        return true;
    } else {
        senhaInput.classList.remove('invalid');
        senhaRequisito.style.color = '#ed8936';
        senhaRequisito.classList.remove('valido');
        return false;
    }
}

// Validação de ISBN em tempo real
function validarISBN() {
    const isbnInput = document.getElementById('livroIsbn');
    const isbnRequisito = document.getElementById('isbnRequisito');
    const isbn = isbnInput.value.trim();
    
    const apenasNumeros = isbn.replace(/\D/g, '');
    
    if (apenasNumeros.length > 0 && apenasNumeros.length !== 13) {
        isbnInput.classList.add('invalid');
        if (isbnRequisito) isbnRequisito.style.color = '#ed8936';
        if (isbnRequisito) isbnRequisito.classList.remove('valido');
        return false;
    } else if (apenasNumeros.length === 13) {
        isbnInput.classList.remove('invalid');
        if (isbnRequisito) isbnRequisito.style.color = '#7eadb3';
        if (isbnRequisito) isbnRequisito.classList.add('valido');
        return true;
    } else {
        isbnInput.classList.remove('invalid');
        if (isbnRequisito) isbnRequisito.style.color = '#ed8936';
        if (isbnRequisito) isbnRequisito.classList.remove('valido');
        return false;
    }
}

// Formatar ISBN enquanto digita (apenas números)
function formatarISBN(e) {
    let valor = e.target.value;
    valor = valor.replace(/\D/g, '');
    if (valor.length > 13) {
        valor = valor.slice(0, 13);
    }
    e.target.value = valor;
}

// Validação de Ano em tempo real
function validarAno() {
    const anoInput = document.getElementById('livroAno');
    const anoRequisito = document.getElementById('anoRequisito');
    const ano = anoInput.value;
    
    const anoRegex = /^\d{4}$/;
    
    if (ano.length > 0 && !anoRegex.test(ano)) {
        anoInput.classList.add('invalid');
        if (anoRequisito) anoRequisito.style.color = '#ed8936';
        if (anoRequisito) anoRequisito.classList.remove('valido');
        return false;
    } else if (anoRegex.test(ano)) {
        anoInput.classList.remove('invalid');
        if (anoRequisito) anoRequisito.style.color = '#7eadb3';
        if (anoRequisito) anoRequisito.classList.add('valido');
        return true;
    } else {
        anoInput.classList.remove('invalid');
        if (anoRequisito) anoRequisito.style.color = '#ed8936';
        if (anoRequisito) anoRequisito.classList.remove('valido');
        return false;
    }
}

// Formatar Ano enquanto digita (apenas números, máximo 4)
function formatarAno(e) {
    let valor = e.target.value;
    valor = valor.replace(/\D/g, '');
    if (valor.length > 4) {
        valor = valor.slice(0, 4);
    }
    e.target.value = valor;
}

function showRegister() {
    document.getElementById('loginCard').classList.add('hidden');
    document.getElementById('registerCard').classList.remove('hidden');
    const senhaInput = document.getElementById('registerSenha');
    const senhaRequisito = document.getElementById('senhaRequisito');
    senhaInput.classList.remove('invalid');
    senhaRequisito.style.color = '#ed8936';
    senhaRequisito.classList.remove('valido');
    senhaInput.value = '';
}

function showLogin() {
    document.getElementById('registerCard').classList.add('hidden');
    document.getElementById('loginCard').classList.remove('hidden');
    document.getElementById('registerNome').value = '';
    document.getElementById('registerEmail').value = '';
    document.getElementById('registerSenha').value = '';
    document.getElementById('loginEmail').value = '';
    document.getElementById('loginSenha').value = '';
    document.getElementById('registerSenha').classList.remove('invalid');
}

async function register() {
    const nome = document.getElementById('registerNome').value.trim();
    const email = document.getElementById('registerEmail').value.trim();
    const senha = document.getElementById('registerSenha').value;

    if (!nome || !email || !senha) {
        showToast('⚠️ Preencha todos os campos!', 'error');
        return;
    }

    if (senha.length < 6) {
        showToast('⚠️ A senha deve ter no mínimo 6 caracteres!', 'error');
        document.getElementById('registerSenha').focus();
        return;
    }

    try {
        const response = await fetch(`${API_URL}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, email, senha })
        });

        if (response.ok) {
            showToast('✅ Cadastro realizado com sucesso! Faça login.', 'success');
            showLogin();
        } else {
            const error = await response.json();
            if (error.fields) {
                let errorMsg = '';
                for (const [field, message] of Object.entries(error.fields)) {
                    errorMsg += `${field}: ${message}\n`;
                }
                showToast(`❌ ${errorMsg}`, 'error');
            } else {
                showToast(`❌ Erro: ${error.message || 'Erro ao cadastrar'}`, 'error');
            }
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('❌ Erro ao conectar com o servidor', 'error');
    }
}

async function login() {
    const email = document.getElementById('loginEmail').value.trim();
    const senha = document.getElementById('loginSenha').value;

    if (!email || !senha) {
        showToast('⚠️ Preencha email e senha!', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (response.ok) {
            const data = await response.json();
            token = data.token;
            userId = data.userId;
            userEmail = data.email;

            document.getElementById('loginCard').classList.add('hidden');
            document.getElementById('livrosCard').classList.remove('hidden');
            
            await fetchUserData();
            showUserMenu();
            
            showToast('✅ Login realizado com sucesso!', 'success');
            carregarLivros();
        } else {
            const error = await response.json();
            showToast(`❌ Login falhou: ${error.message || 'Email ou senha inválidos'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('❌ Erro ao conectar com o servidor', 'error');
    }
}

async function carregarLivros() {
    try {
        const response = await fetch(`${API_URL}/livros`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const livros = await response.json();
            mostrarLivros(livros);
        }
    } catch (error) {
        console.error('Erro ao carregar livros:', error);
        showToast('❌ Erro ao carregar livros', 'error');
    }
}

function mostrarLivros(livros) {
    const container = document.getElementById('lista-livros');
    if (livros.length === 0) {
        container.innerHTML = '<p>Nenhum livro cadastrado.</p>';
        return;
    }

    container.innerHTML = livros.map(livro => `
        <div class="livro-card">
            <h3>${escapeHtml(livro.titulo)}</h3>
            <p><strong>Autor:</strong> ${escapeHtml(livro.autor)}</p>
            <p><strong>ISBN:</strong> ${livro.isbn || 'N/A'}</p>
            <p><strong>Ano:</strong> ${livro.anoPublicacao || 'N/A'}</p>
            <p><strong>Categoria:</strong> ${escapeHtml(livro.categoria || 'N/A')}</p>
            <p><strong>Descrição:</strong> ${escapeHtml(livro.descricao || 'N/A')}</p>
            <p><strong>Status:</strong> <span class="status status-${livro.status.toLowerCase()}">${livro.status}</span></p>
            <div class="flex">
                <button onclick="editarLivro('${livro.id}')">Editar</button>
                <button class="danger" onclick="excluirLivro('${livro.id}')">Excluir</button>
            </div>
        </div>
    `).join('');
}

// Função de excluir com toast de confirmação (CORRIGIDA)
function excluirLivro(id) {
    showConfirmToast('Tem certeza que deseja excluir este livro?', async () => {
        try {
            const response = await fetch(`${API_URL}/livros/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (response.ok) {
                showToast('✅ Livro excluído com sucesso!', 'success');
                carregarLivros();
            } else {
                showToast('❌ Erro ao excluir livro', 'error');
            }
        } catch (error) {
            console.error('Erro:', error);
            showToast('❌ Erro: ' + error.message, 'error');
        }
    });
}

async function criarLivro() {
    // Se estiver editando, não deve criar novo
    if (livroEditandoId) {
        showToast('⚠️ Você está editando um livro. Clique em "Atualizar Livro" para salvar as alterações.', 'info');
        return;
    }
    
    const titulo = document.getElementById('livroTitulo').value.trim();
    const autor = document.getElementById('livroAutor').value.trim();
    const isbn = document.getElementById('livroIsbn').value.trim();
    const ano = document.getElementById('livroAno').value.trim();
    const categoria = document.getElementById('livroCategoria').value.trim();
    const descricao = document.getElementById('livroDescricao').value.trim();
    const status = document.getElementById('livroStatus').value;

    if (!titulo) {
        showToast('⚠️ Título é obrigatório!', 'error');
        document.getElementById('livroTitulo').focus();
        return;
    }
    if (!autor) {
        showToast('⚠️ Autor é obrigatório!', 'error');
        document.getElementById('livroAutor').focus();
        return;
    }
    
    const isbnNumeros = isbn.replace(/\D/g, '');
    if (isbn && isbnNumeros.length !== 13) {
        showToast('⚠️ ISBN deve ter exatamente 13 dígitos numéricos!', 'error');
        document.getElementById('livroIsbn').focus();
        return;
    }
    
    const anoRegex = /^\d{4}$/;
    if (ano && !anoRegex.test(ano)) {
        showToast('⚠️ Ano deve ter 4 dígitos (ex: 2024)!', 'error');
        document.getElementById('livroAno').focus();
        return;
    }

    const livro = {
        titulo: titulo,
        autor: autor,
        isbn: isbnNumeros || null,
        anoPublicacao: ano ? parseInt(ano) : null,
        categoria: categoria || null,
        descricao: descricao || null,
        status: status
    };

    try {
        const response = await fetch(`${API_URL}/livros`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(livro)
        });

        if (response.ok) {
            showToast('✅ Livro adicionado com sucesso!', 'success');
            resetarFormularioLivro();
            carregarLivros();
        } else {
            const error = await response.json();
            if (error.fields) {
                let errorMsg = '';
                for (const [field, message] of Object.entries(error.fields)) {
                    errorMsg += `${field}: ${message}\n`;
                }
                showToast(`❌ ${errorMsg}`, 'error');
            } else {
                showToast(`❌ Erro: ${error.message || 'Erro ao criar livro'}`, 'error');
            }
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('❌ Erro ao conectar com o servidor: ' + error.message, 'error');
    }
}

// Função auxiliar para escapar HTML
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Configurar eventos após o carregamento da página
document.addEventListener('DOMContentLoaded', function() {
    const senhaInput = document.getElementById('registerSenha');
    if (senhaInput) {
        senhaInput.addEventListener('input', validarSenha);
    }
    
    const isbnInput = document.getElementById('livroIsbn');
    if (isbnInput) {
        isbnInput.addEventListener('input', validarISBN);
        isbnInput.addEventListener('input', formatarISBN);
    }
    
    const anoInput = document.getElementById('livroAno');
    if (anoInput) {
        anoInput.addEventListener('input', validarAno);
        anoInput.addEventListener('input', formatarAno);
    }
    
    const userTrigger = document.getElementById('userTrigger');
    if (userTrigger) {
        userTrigger.addEventListener('click', toggleDropdown);
    }
    
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
    
    document.addEventListener('click', closeDropdownOnClickOutside);
});