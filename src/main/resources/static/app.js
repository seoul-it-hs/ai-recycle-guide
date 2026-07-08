const analyzeForm = document.getElementById('analyzeForm');
const imageInput = document.getElementById('imageInput');
const previewImage = document.getElementById('previewImage');
const previewPlaceholder = document.getElementById('previewPlaceholder');
const analyzeButton = document.getElementById('analyzeButton');
const loadingMessage = document.getElementById('loadingMessage');
const errorMessage = document.getElementById('errorMessage');
const resultArea = document.getElementById('resultArea');

const itemName = document.getElementById('itemName');
const category = document.getElementById('category');
const guide = document.getElementById('guide');
const caution = document.getElementById('caution');

imageInput.addEventListener('change', () => {
	const file = imageInput.files[0];

	clearMessages();
	hideResult();

	if (!file) {
		clearPreview();
		return;
	}

	if (!file.type.startsWith('image/')) {
		clearPreview();
		showError('이미지 파일만 선택할 수 있습니다.');
		return;
	}

	previewImage.src = URL.createObjectURL(file);
	previewImage.classList.add('is-visible');
	previewPlaceholder.hidden = true;
});

analyzeForm.addEventListener('submit', async (event) => {
	event.preventDefault();

	const file = imageInput.files[0];

	clearMessages();
	hideResult();

	if (!file) {
		showError('분석할 이미지 파일을 선택해주세요.');
		return;
	}

	if (!file.type.startsWith('image/')) {
		showError('이미지 파일만 업로드할 수 있습니다.');
		return;
	}

	const formData = new FormData();
	formData.append('file', file);

	setLoading(true);

	try {
		const response = await fetch('/api/analyze', {
			method: 'POST',
			body: formData
		});

		if (!response.ok) {
			const message = await response.text();
			throw new Error(message || '분석 요청에 실패했습니다.');
		}

		const result = await response.json();
		renderResult(result);
	} catch (error) {
		showError(error.message);
	} finally {
		setLoading(false);
	}
});

function clearPreview() {
	previewImage.removeAttribute('src');
	previewImage.classList.remove('is-visible');
	previewPlaceholder.hidden = false;
}

function clearMessages() {
	errorMessage.hidden = true;
	errorMessage.textContent = '';
	loadingMessage.hidden = true;
}

function setLoading(isLoading) {
	loadingMessage.hidden = !isLoading;
	analyzeButton.disabled = isLoading;
	analyzeButton.textContent = isLoading ? '분석 중...' : '분석하기';
}

function showError(message) {
	errorMessage.textContent = message;
	errorMessage.hidden = false;
}

function hideResult() {
	resultArea.hidden = true;
	itemName.textContent = '';
	category.textContent = '';
	guide.textContent = '';
	caution.textContent = '';
}

function renderResult(result) {
	itemName.textContent = result.itemName || '-';
	category.textContent = result.category || '-';
	guide.textContent = result.guide || '-';
	caution.textContent = result.caution || '-';
	resultArea.hidden = false;
}
