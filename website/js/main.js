// Scroll to top
const offset = 300,
offsetOpacity = 1200,
scrollTopDuration = 700,
backToTop = document.querySelector('.cdTop');

window.addEventListener('scroll', function(e) {

	if(document.body.scrollTop > offset || document.documentElement.scrollTop > offset){
		backToTop.classList.add("cd-is-visible");
	}else{
		backToTop.classList.remove("cd-is-visible");
		backToTop.classList.remove("cd-fade-out");
	}

	if(document.body.scrollTop > offsetOpacity || document.documentElement.scrollTop > offsetOpacity){
		backToTop.classList.add("cd-fade-out");
	}
});

backToTop.addEventListener('click', () => {
	(function smoothscroll(){
		var currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
		if (currentScroll > 0) {
			 window.requestAnimationFrame(smoothscroll);
			 window.scrollTo (0,currentScroll - (currentScroll/5));
		}
	})();
});

/* Get Our Elements */
const player = document.querySelector('.player');
const video = player.querySelector('video');
const progress = player.querySelector('.progress');
const progressBar = player.querySelector('.progressFilled');
const ranges = player.querySelectorAll('.playerSlider');

/* Build out functions */
function togglePlay() {
  const method = video.paused ? 'play' : 'pause';
  video[method]();
}

function handleRangeUpdate() {
  video[this.name] = this.value;
}

function handleProgress() {
  const percent = (video.currentTime / video.duration) * 100;
  progressBar.style.flexBasis = `${percent}%`;
}

function scrub(e) {
  const scrubTime = (e.offsetX / progress.offsetWidth) * video.duration;
  video.currentTime = scrubTime;
}

/* Hook up the event listners */
video.addEventListener('click', togglePlay);
video.addEventListener('timeupdate', handleProgress);

ranges.forEach(range => range.addEventListener('change', handleRangeUpdate));
ranges.forEach(range => range.addEventListener('mousemove', handleRangeUpdate));

let mousedown = false;
progress.addEventListener('click', scrub);
progress.addEventListener('mousemove', (e) => mousedown && scrub(e));
progress.addEventListener('mousedown', () => mousedown = true);
progress.addEventListener('mouseup', () => mousedown = false);

 window.onload = function() {
	video.play();
 }
