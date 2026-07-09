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

let previewImageUrl = '';

// change 이벤트는 서버로 보내기 전에 선택한 이미지를 화면에서 미리 보여준다.
imageInput.addEventListener('change', () => {
	const file = imageInput.files[0];

	clearMessages();
	hideResult();

	if (!file) {
		clearPreview();
		setAnalyzeButtonEnabled(false);
		return;
	}

	if (!isImageFile(file)) {
		clearPreview();
		setAnalyzeButtonEnabled(false);
		showError('이미지 파일만 선택할 수 있습니다.');
		return;
	}

	showPreview(file);
	setAnalyzeButtonEnabled(true);
});

// submit 이벤트는 선택한 이미지를 Spring Boot 서버로 보내고 분석 결과를 받는다.
analyzeForm.addEventListener('submit', async (event) => {
	event.preventDefault();

	const file = imageInput.files[0];

	clearMessages();
	hideResult();

	if (!file) {
		showError('분석할 이미지 파일을 선택해주세요.');
		return;
	}

	if (!isImageFile(file)) {
		showError('이미지 파일만 업로드할 수 있습니다.');
		return;
	}

	setLoading(true);

	try {
		const result = await requestAnalysis(file);
		renderResult(result);
	} catch (error) {
		showError(error.message);
	} finally {
		setLoading(false);
	}
});

function isImageFile(file) {
	return file.type.startsWith('image/');
}

function showPreview(file) {
	clearPreview();
	previewImageUrl = URL.createObjectURL(file);
	previewImage.src = previewImageUrl;
	previewImage.classList.add('is-visible');
	previewPlaceholder.hidden = true;
}

function clearPreview() {
	if (previewImageUrl) {
		URL.revokeObjectURL(previewImageUrl);
		previewImageUrl = '';
	}

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
	imageInput.disabled = isLoading;
	setAnalyzeButtonEnabled(!isLoading);
	analyzeButton.textContent = isLoading ? '분석 중...' : '분석하기';
}

function setAnalyzeButtonEnabled(isEnabled) {
	analyzeButton.disabled = !isEnabled;
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

async function requestAnalysis(file) {
	const response = await fetch('/api/analyze', {
		method: 'POST',
		body: createImageFormData(file)
	});

	if (!response.ok) {
		const message = await response.text();
		throw new Error(message || '분석 요청에 실패했습니다.');
	}

	return response.json();
}

function createImageFormData(file) {
	const formData = new FormData();
	formData.append('file', file);
	return formData;
}

function renderResult(result) {
	itemName.textContent = result.itemName || '-';
	category.textContent = result.category || '-';
	guide.textContent = result.guide || '-';
	caution.textContent = result.caution || '-';
	resultArea.hidden = false;
}
